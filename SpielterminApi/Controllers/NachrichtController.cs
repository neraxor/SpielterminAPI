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
    public class NachrichtController : ControllerBase
    {
        private readonly SpielterminDbContext _context;
        private readonly ISpielerService _userService;

        public NachrichtController(SpielterminDbContext context, ISpielerService userService)
        {
            _context = context;
            _userService = userService;
        }

        /// <summary>
        /// Erzeugt eine neue Nachricht. Keine doppelten Nachrichten möglich.
        /// </summary>
        /// <param name="request">Benötigt SpielgruppenId und einen Nachrichtentext</param>
        /// <returns></returns>
        [HttpPost("create-nachricht"), Authorize]
        public async Task<ActionResult<Nachricht>> CreateNachricht(NachrichtDto request)
        {
            int SpielerId = _userService.GetSpielerId();
            var nachrichtExists = _context.Nachrichten.Any(n => n.AbsenderId == SpielerId && n.SpielgruppeId == request.SpielgruppeId  && n.SpielterminId == request.SpielterminId);

            if (nachrichtExists)
            {
                return BadRequest("Verspätung bereits gesendet");
            }
            var isSpielerInSpielgruppe = await _context.SpielgruppeSpieler.AnyAsync(x => x.SpielgruppeId == request.SpielgruppeId && x.SpielerId == SpielerId);
            if (!isSpielerInSpielgruppe)
            {
                return Unauthorized("Sie sind nicht in der Spielgruppe für diese Verspätungen");
            }
            var nachricht = new Nachricht
            {
                AbsenderId = SpielerId,
                SpielgruppeId = request.SpielgruppeId,
                NachrichtText = request.NachrichtText,
                SpielterminId = request.SpielterminId,
                Uhrzeit = DateTime.Now 
            };
            _context.Nachrichten.Add(nachricht);

            if (await _context.SaveChangesAsync() > 0)
            {
                var nachrichtResponse = new NachrichtDto
                {
                    AbsenderId = nachricht.AbsenderId,
                    SpielgruppeId = nachricht.SpielgruppeId,
                    NachrichtText = nachricht.NachrichtText,
                    Uhrzeit = nachricht.Uhrzeit
                };
                //return Ok(nachrichtResponse);
                return Ok("Verspätung gesendet");
            }

            return BadRequest("Verspätung konnte nicht gespeichert werden");
        }
        /// <summary>
        /// Liefert alle Nachrichten einer Spielgruppe absteigend sortiert nach Uhrzeit
        /// </summary>
        /// <param name="spielgruppeId">Benötigt Spielergruppe</param>
        /// <param name="anzahlNachrichten">Wenn limitANzahl true dann ist das hier die Anzahl an Nachrichten</param>
        /// <param name="limitAnzahl">Gibt an ob alle Nachrichten oder nur eine begrenzte Anzahl returned werden</param>
        /// <returns></returns>
        [HttpGet("get-nachrichten"), Authorize]
        public async Task<ActionResult<IEnumerable<NachrichtDto>>> GetNachrichten(int SpielerId, int spielgruppeId,int anzahlNachrichten = 10, bool limitAnzahl = false)
        {
            //int SpielerId = _userService.GetSpielerId();

            var isSpielerInSpielgruppe = await _context.SpielgruppeSpieler.AnyAsync(x => x.SpielgruppeId == spielgruppeId && x.SpielerId == SpielerId);
            if (!isSpielerInSpielgruppe)
            {
                return Unauthorized("Sie sind nicht in der Spielgruppe für diese Nachrichten");
            }
            var nachrichten = new List<Nachricht>();
            if (limitAnzahl)
            {
                nachrichten = await _context.Nachrichten.Where(n => n.SpielgruppeId == spielgruppeId).OrderByDescending(n => n.Uhrzeit).Take(anzahlNachrichten).ToListAsync();
            }
            else
            {
                nachrichten = await _context.Nachrichten.Where(n => n.SpielgruppeId == spielgruppeId).OrderByDescending(n => n.Uhrzeit).ToListAsync();
            }

            var nachrichtenResponse = nachrichten.Select(n => new NachrichtDto
            {
                ID = n.ID,
                AbsenderId = n.AbsenderId,
                SpielgruppeId = n.SpielgruppeId,
                NachrichtText = n.NachrichtText,
                Uhrzeit = n.Uhrzeit
            }).ToList();
            return Ok(nachrichtenResponse);
        }
    }
}
