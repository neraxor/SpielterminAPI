using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using SpielterminApi.Models;
using WebApplication1.Services;

namespace SpielterminApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SpielterminController : ControllerBase
    {
        private readonly SpielterminDbContext _context;
        private readonly ISpielerService _userService;

        public SpielterminController(SpielterminDbContext context, ISpielerService userService)
        {
            _context = context;
            _userService = userService;
        }

        /// <summary>
        /// Gibt alle Spieltermin für einen Spieler zurück. Optional bei filterByDate = true nur Termine ab heute.
        /// </summary>
        /// <param name="filterByDate">true liefert nur termine ab heute, false alle </param>
        /// <returns></returns>
        [HttpGet, Authorize]
        [Route("/GetSpieltermineBySpieler")]
        public async Task<ActionResult<IEnumerable<Spieltermin>>> GetSpieltermineBySpieler(bool filterByDate = false)
        {
            int SpielerId = _userService.GetSpielerId();
            var Spielgruppen = await _context.SpielgruppeSpieler
                .Where(x => x.SpielerId == SpielerId)
                .Select(x => x.SpielgruppeId)
                .ToListAsync();


            var spieltermine = await _context.Spieltermine
                                 .Where(x =>  Spielgruppen.Contains(x.SpielgruppeId))
                                 //.Where(x =>  Spielgruppen.Contains(x.SpielgruppeId) && x.Termin > DateTime.Now)
                                 .OrderBy(x => x.Termin)
                                 .ToListAsync();
            //optionaler Filter für das Datum
            if (filterByDate)
            {
                  spieltermine = spieltermine.Where(x => x.Termin.Date >= DateTime.Now.Date).ToList();
            }
            return spieltermine;
        }
        /// <summary>
        /// Gibt alle Spieltermin für eine Gruppe zurück. Optional bei filterByDate = true nur Termine ab heute.
        /// </summary>
        /// <param name="filterByDate">true liefert nur termine ab heute, false alle</param>
        /// <param name="SpielgruppenId">Gruppennummer</param>
        /// <returns></returns>
        [HttpGet, Authorize]
        [Route("/GetSpieltermineByGruppe")]
        public async Task<ActionResult<IEnumerable<Spieltermin>>> GetSpieltermineByGruppe(bool filterByDate = false, int SpielgruppenId = 0)
        {
            int SpielerId = _userService.GetSpielerId();
                //var Spielgruppen = await _context.SpielgruppeSpieler
                //.Where(x => x.SpielerId == SpielerId && x.SpielgruppeId == SpielgruppenId)
                //.Select(x => x.ID)
                //.ToListAsync();
            var spielgruppeSpieler = await _context.SpielgruppeSpieler
                .FirstOrDefaultAsync(x => x.SpielerId == SpielerId && x.SpielgruppeId == SpielgruppenId);


            if (spielgruppeSpieler != null)
            {

                var spieltermine = await _context.Spieltermine
                                     .Where(x => x.SpielgruppeId == SpielgruppenId)
                                     //.Where(x => Spielgruppen.Contains(x.SpielgruppeId))
                                     //.Where(x =>  Spielgruppen.Contains(x.SpielgruppeId) && x.Termin > DateTime.Now)
                                     .OrderBy(x => x.Termin)
                                     .ToListAsync();
                //optionaler Filter für das Datum
                if (filterByDate)
                {
                    spieltermine = spieltermine.Where(x => x.Termin.Date >= DateTime.Now.Date).ToList();
                }
                return spieltermine;
            }
            return BadRequest("Keine Spielgruppe für deinen Nutzer gefunden.");
        }
        /// <summary>
        /// Gibt die Adresse des Gastgebers für einen Spieltermin zurück. Spieler muss jedoch in der Spielegruppe des Termins sein.
        /// </summary>
        /// <param name="spielterminId"></param>
        /// <returns>Liefer Wohnort, PLZ, Straße, Hausnummer</returns>
        [HttpGet, Authorize]
        [Route("/GetGastgeberAdresse")]
        public async Task<ActionResult<SpielerDto>> GetGastgeberAdresse(int spielterminId)
        {
            int SpielerId = _userService.GetSpielerId();
            var spieltermin = await _context.Spieltermine.Where(x => x.ID == spielterminId).FirstOrDefaultAsync();
            if (spieltermin == null)
            {
                return NotFound("Spieltermin nicht gefunden.");
            }
            var isSpielerInSpielgruppe = await _context.SpielgruppeSpieler.AnyAsync(x => x.SpielgruppeId == spieltermin.SpielgruppeId && x.SpielerId == SpielerId);
            if (!isSpielerInSpielgruppe)
            {
                return Unauthorized("Sie sind nicht in der Spielgruppe für diesen Spieltermin.");
            }
            if (spieltermin != null)
            {
                var gastgeber = await _context.Spieler.FirstOrDefaultAsync(x => x.ID == spieltermin.GastgeberId);
                if (gastgeber != null)
                {
                    var gastgeberAdresse = new SpielerDto();
                    gastgeberAdresse.Wohnort = gastgeber.Wohnort;
                    gastgeberAdresse.PLZ = gastgeber.PLZ;
                    gastgeberAdresse.Straße = gastgeber.Straße;
                    gastgeberAdresse.Hausnummer = gastgeber.Hausnummer;
                    //var Adresse = spieler.Wohnort + " " + spieler.PLZ + " " + spieler.Straße + " " + spieler.Hausnummer;  
                    return gastgeberAdresse;
                }
            }
            return NotFound("Spieltermin oder Gastgeber nicht gefunden.");
        }

        /// <summary>
        /// Erzeugt einen neuen Spieltermin für eine Spielgruppe. Der Gastgeber wird automatisch aus der Spielgruppe ausgewählt.
        /// </summary>
        /// <param name="request">Benötigt die SpielgruppenId und den Termin.</param>
        /// <returns></returns>
        [HttpPost("create-spieltermin"), Authorize]
        public async Task<ActionResult<Spieltermin>> CreateSpieltermin(SpielterminDto request)
        {
            var spielterminExists = _context.Spieltermine.Any(st =>  st.Termin.Date == request.Termin.Date && st.SpielgruppeId == request.SpielgruppeId);

            if (spielterminExists)
            {
                return BadRequest("Spieltermin existiert bereits für diese Spielgruppe und diesen Tag.");
            }
            var NextGastgeber = _context.SpielgruppeSpieler.Where(x => x.SpielgruppeId == request.SpielgruppeId && x.WarGastgeber == false).FirstOrDefault();
            if (NextGastgeber == null)
            {
                var EntitiesToUpdate = _context.SpielgruppeSpieler.Where(x => x.SpielgruppeId == request.SpielgruppeId && x.WarGastgeber == true).ToList();
                foreach (SpielgruppeSpieler elem in EntitiesToUpdate)
                {
                    elem.WarGastgeber = false;
                }
                NextGastgeber = EntitiesToUpdate.FirstOrDefault();
            }
            if (NextGastgeber != null)
            {
                NextGastgeber.WarGastgeber = true;
            }
            var spieltermin = new Spieltermin
            {
                Termin = request.Termin,  
                SpielgruppeId = request.SpielgruppeId,
                GastgeberId = NextGastgeber.SpielerId
            };

            _context.Spieltermine.Add(spieltermin);

            if (await _context.SaveChangesAsync() > 0)
            {
                return Ok(spieltermin);
            }

            return BadRequest("Spieltermin konnte nicht gespeichert werden.");
        }

    }
}
