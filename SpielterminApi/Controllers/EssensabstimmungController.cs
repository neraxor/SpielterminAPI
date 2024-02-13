using Azure.Core;
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
    public class EssensabstimmungController : ControllerBase
    {
        private readonly SpielterminDbContext _context;
        private readonly ISpielerService _userService;

        public EssensabstimmungController(SpielterminDbContext context, ISpielerService userService)
        {
            _context = context;
            _userService = userService;
        }

        /// <summary>
        /// Erzeugt eine neue Essensabstimmung. Keine doppelten Abstimmungen möglich.
        /// </summary>
        /// <param name="request">benötigt SpielterminId und EssensrichtungId</param>
        /// <returns></returns>
        [HttpPost("Essenabstimmen"), Authorize]

        public async Task<ActionResult<EssensabstimmungDto>> Esssenabstimmen(EssensabstimmungDto request)
        {
            int SpielerId = _userService.GetSpielerId();

            var bereitsAbgestimmt = await _context.Essensabstimmungen.AnyAsync(e => e.SpielerId == SpielerId && e.SpielterminId == request.SpielterminId);
            if (bereitsAbgestimmt)
            {
                return BadRequest("Spieler hat bereits für diesen Spieltermin abgestimmt");
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

            var abstimmung = new Essensabstimmung
            {
                EssensrichtungId = request.EssensrichtungId,
                SpielerId = SpielerId,
                SpielterminId = request.SpielterminId
            };

            _context.Essensabstimmungen.Add(abstimmung);
            if (await _context.SaveChangesAsync()>0)
            {
                var abstimmungResponse = new EssensabstimmungDto
                {
                    ID = abstimmung.ID,
                    EssensrichtungId = abstimmung.EssensrichtungId,
                    SpielerId = abstimmung.SpielerId,
                    SpielterminId = abstimmung.SpielterminId
                };
                return Ok("Abstimmung wurde gespeichert.");
                //return Ok(abstimmungResponse);
            }
            return BadRequest("Abstimmung konnte nicht gespeichert werden");          
        }

        /// <summary>
        /// Gibt die Essensrichtung mit den meisten Stimmen zurück. Gleichstand gibt die erste Essensrichtung zurück.
        /// </summary>
        /// <param name="spielterminId">SpielterminId benötigt</param>
        /// <returns></returns>
        [HttpGet("GetSiegerEssensrichtung")]
        public async Task<ActionResult<string>> GetSiegerEssensrichtung(int spielterminId)
        {
            int SpielerId = _userService.GetSpielerId();

            var spieltermin = await _context.Spieltermine.Include(x => x.Spielgruppe).ThenInclude(x => x.SpielgruppeSpieler).FirstOrDefaultAsync(x => x.ID == spielterminId);
            if (spieltermin == null)
            {
                return BadRequest("Spieltermin existiert nicht");
            }
            var spielerInGruppe = spieltermin.Spielgruppe.SpielgruppeSpieler.Any(x => x.SpielerId == SpielerId);
            if (!spielerInGruppe)
            {
                return Unauthorized("Spieler ist nicht in der Spielgruppe des Spieltermins");
            }

            var meistGestimmt = await _context.Essensabstimmungen
                .Where(e => e.SpielterminId == spielterminId)
                .GroupBy(e => e.Essensrichtung)
                .Select(group => new
                {
                    Essensrichtung = group.Key,
                    Anzahl = group.Count()
                })
                .OrderByDescending(e => e.Anzahl)
                .FirstOrDefaultAsync();

            if (meistGestimmt == null)
            {
                return NotFound("Keine Abstimmungen für diesen Spieltermin gefunden.");
            }
            return Ok(meistGestimmt.Essensrichtung);
        }

    }
}
