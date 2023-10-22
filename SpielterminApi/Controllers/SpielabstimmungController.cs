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
    public class SpielabstimmungController : ControllerBase
    {
        private readonly SpielterminDbContext _context;
        private readonly ISpielerService _userService;

        public SpielabstimmungController(SpielterminDbContext context, ISpielerService userService)
        {
            _context = context;
            _userService = userService;
        }

        /// <summary>
        /// Lässt den Benutzer über einen Spielvorschlag abstimmen. Keine doppelten Abstimmungen möglich.
        /// </summary>
        /// <param name="request">SpielerId,SpielgruppeId benötigt</param>
        /// <returns></returns>
        [HttpPost("create-spielabstimmung"), Authorize]
        public async Task<ActionResult<Spielabstimmung>> CreateSpielabstimmung(SpielabstimmungDto request)
        {
            int SpielerId = _userService.GetSpielerId();
            var spielvorschlagExists = _context.Spielvorschlaege.Any(x => x.ID == request.SpielvorschlagId);
            if (!spielvorschlagExists)
            {
                return BadRequest("Spielvorschlag existiert nicht");
            }
            var abstimmungExists = _context.Spielabstimmungen.Any(x => x.ID == request.SpielvorschlagId && x.SpielvorschlagId == request.SpielerId && x.SpielgruppeId == request.SpielgruppeId);
            if (abstimmungExists)
            {
                return BadRequest("Benutzer hat bereits abgestimmt");
            }
            var isSpielerInSpielgruppe = await _context.SpielgruppeSpieler.AnyAsync(x => x.SpielgruppeId == request.SpielgruppeId && x.SpielerId == SpielerId);
            if (!isSpielerInSpielgruppe)
            {
                return Unauthorized("Sie sind nicht in der Spielgruppe für diesen Spieltermin");
            }
            var spielabstimmung = new Spielabstimmung
            {
                SpielvorschlagId = request.SpielvorschlagId,
                SpielerId = request.SpielerId,
                Zustimmung = request.Zustimmung,
                SpielgruppeId = request.SpielgruppeId
            };
            _context.Spielabstimmungen.Add(spielabstimmung);

            if (await _context.SaveChangesAsync() > 0)
            {
                var spielabstimmungResponse =  new SpielabstimmungDto
                {
                    SpielvorschlagId = spielabstimmung.SpielvorschlagId,
                    SpielerId = spielabstimmung.SpielerId,
                    Zustimmung = spielabstimmung.Zustimmung,
                    SpielgruppeId = spielabstimmung.SpielgruppeId
                };
                return Ok(spielabstimmungResponse);
            }

            return BadRequest("Spielabstimmung konnte nicht gespeichert werden");
        }
    }
}
