using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using SpielterminApi.Models;
using WebApplication1.Services;

namespace SpielterminApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AbendbewertungController : ControllerBase
    {
        private readonly SpielterminDbContext _context;
        private readonly ISpielerService _userService;

        public AbendbewertungController(SpielterminDbContext context, ISpielerService userService)
        {
            _context = context;
            _userService = userService;
        }
        /// <summary>
        /// Fügt eine neue Abendbewertung hinzu. Spieler muss in der Spielgruppe des Spieltermins sein. Keine doppelten Abendbewertungen möglich.
        /// </summary>
        /// <param name="request">SpielerterminId benötigt & Bewertungen in form von Int für  Gastgeber, Essen und Abend</param>
        /// <returns></returns>
        [HttpPost("create-abendbewertung"), Authorize]
        public async Task<ActionResult<Abendbewertung>> CreateAbendbewertung(AbendbewertungDto request)
        {
            int SpielerId = _userService.GetSpielerId();

            var bewertungExists = _context.Abendbewertungen.Any(x => x.SpielterminId == request.SpielterminId && x.SpielerId == request.SpielerId);
            if (bewertungExists)
            {
                return BadRequest("Abendbewertung existiert bereits");
            }

            var spieltermin = await _context.Spieltermine.Include(x => x.Spielgruppe).ThenInclude(x => x.SpielgruppeSpieler).FirstOrDefaultAsync(x => x.ID == request.SpielterminId);

            if (spieltermin == null)
            {
                return BadRequest("Spieltermin existiert nicht");
            }

            var spielerInGruppe = spieltermin.Spielgruppe.SpielgruppeSpieler.Any(x => x.SpielerId == SpielerId);
            if (!spielerInGruppe)
            {
                return Unauthorized("Spieler ist nicht in der Spielgruppe des Spieltermins");
            }
            var abendbewertung = new Abendbewertung
            {
                SpielterminId = request.SpielterminId,
                SpielerId = SpielerId,
                Gastgeberbewertung = request.Gastgeberbewertung,
                Essensbewertung = request.Essensbewertung,
                AbendbewertungName = request.AbendbewertungName,
            };
             _context.Abendbewertungen.Add(abendbewertung);
            
            if (await _context.SaveChangesAsync() > 0)
            {
                var abendbewertungResponse = new AbendbewertungDto
                {
                    SpielterminId = abendbewertung.SpielterminId,
                    SpielerId = abendbewertung.SpielerId,
                    Gastgeberbewertung = abendbewertung.Gastgeberbewertung,
                    Essensbewertung = abendbewertung.Essensbewertung,
                    AbendbewertungName = abendbewertung.AbendbewertungName
                };
                return Ok("Bewertung erfolgreich gespeichert.");
                //return Ok(abendbewertungResponse);
            }

            return BadRequest("Abendbewertung konnte nicht gespeichert werden.");
        }

        /// <summary>
        /// Liefert einen aufgerundeten Durschnitt der Bewertungen für eine Spielgruppe
        /// </summary>
        /// <param name="spielgruppeId">benötigt spielgruppeId</param>
        /// <returns></returns>
        [HttpGet("GetBewertungenByGruppe"), Authorize]
        public async Task<ActionResult<IEnumerable<AbendbewertungDto>>> GetGetBewertungenByGruppeBewertungen(int spielgruppeId)
        {
            int SpielerId = _userService.GetSpielerId();
            var isSpielerInSpielgruppe = await _context.SpielgruppeSpieler.AnyAsync(x => x.SpielgruppeId == spielgruppeId && x.SpielerId == SpielerId);
            if (!isSpielerInSpielgruppe)
            {
                return Unauthorized("Sie sind nicht in der Spielgruppe für diese Nachrichten");
            }
            var bewertungen = await _context.Abendbewertungen.Where(x => x.Spieltermin.SpielgruppeId == spielgruppeId).ToListAsync();
            var bewertungResponse = new AbendbewertungDto
            {
                Gastgeberbewertung = 0,
                Essensbewertung = 0,
                AbendbewertungName = 0
            };
            foreach (var bewertung in bewertungen)
            {
                bewertungResponse.Gastgeberbewertung += bewertung.Gastgeberbewertung;
                bewertungResponse.Essensbewertung += bewertung.Essensbewertung;
                bewertungResponse.AbendbewertungName += bewertung.AbendbewertungName;
            }
            var anzahlBewertungen = bewertungen.Count();
            bewertungResponse.Gastgeberbewertung = bewertungResponse.Gastgeberbewertung / anzahlBewertungen;
            bewertungResponse.Essensbewertung = bewertungResponse.Essensbewertung / anzahlBewertungen;
            bewertungResponse.AbendbewertungName = bewertungResponse.AbendbewertungName / anzahlBewertungen;
            return Ok(bewertungResponse);
        }

        /// <summary>
        /// Liefert einen aufgerundeten Durschnitt der Bewertungen für einen Spieltermin
        /// </summary>
        /// <param name="spielterminId">benötigt spielterminId</param>
        /// <returns></returns>
        [HttpGet("GetBewertungenByTermin"), Authorize]
        public async Task<ActionResult<IEnumerable<AbendbewertungDto>>> GetBewertungenByTermin(int spielterminId)
        {
            int SpielerId = _userService.GetSpielerId();
            var spieltermin = await _context.Spieltermine.FirstOrDefaultAsync(x => x.ID == spielterminId);
            if (spieltermin == null)
            {
                return NotFound("Spieltermin nicht gefunden");
            }
            var spielgruppeId = spieltermin.SpielgruppeId;
            var isSpielerInSpielgruppe = await _context.SpielgruppeSpieler.AnyAsync(x => x.SpielgruppeId == spielgruppeId && x.SpielerId == SpielerId);
            if (!isSpielerInSpielgruppe)
            {
                return Unauthorized("Sie sind nicht in der Spielgruppe für diese Nachrichten");
            }
            var bewertungen = await _context.Abendbewertungen.Where(x => x.SpielterminId == spielterminId).ToListAsync();
            var bewertungResponse = new AbendbewertungDto
            {
                Gastgeberbewertung = 0,
                Essensbewertung = 0,
                AbendbewertungName = 0
            };
            foreach (var bewertung in bewertungen)
            {
                bewertungResponse.Gastgeberbewertung += bewertung.Gastgeberbewertung;
                bewertungResponse.Essensbewertung += bewertung.Essensbewertung;
                bewertungResponse.AbendbewertungName += bewertung.AbendbewertungName;
            }
            var anzahlBewertungen = bewertungen.Count();
            //falls nicht aufrunden dann im DTO float statt int
            bewertungResponse.Gastgeberbewertung = bewertungResponse.Gastgeberbewertung / anzahlBewertungen;
            bewertungResponse.Essensbewertung = bewertungResponse.Essensbewertung / anzahlBewertungen;
            bewertungResponse.AbendbewertungName = bewertungResponse.AbendbewertungName / anzahlBewertungen;

            return Ok(bewertungResponse);
        }
    }
}
