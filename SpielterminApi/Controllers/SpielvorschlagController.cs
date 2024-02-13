using System.Collections;
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
    public class SpielvorschlagController : ControllerBase
    {
        private readonly SpielterminDbContext _context;
        private readonly ISpielerService _userService;

        public SpielvorschlagController(SpielterminDbContext context, ISpielerService userService)
        {
            _context = context;
            _userService = userService;
        }

        /// <summary>
        /// Fügt einen neuen Spielvorschlag hinzu. Spieler muss in der Spielgruppe des Spieltermins sein. Keine doppelten Spielvorschläge möglich.
        /// </summary>
        /// <param name="request">Spieltermin muss gegeben werden</param>
        /// <returns></returns>
        [HttpPost("create-spielvorschlag"), Authorize]
        public async Task<ActionResult<Spielvorschlag>> CreateSpielvorschlag(SpielvorschlagDto request)
        {
            int SpielerId = _userService.GetSpielerId();
            var spielvorschlagExists = _context.Spielvorschlaege.Any(x => x.SpielterminId == request.SpielterminId && x.SpielvorschlagName == request.SpielvorschlagName);
            if (spielvorschlagExists)
            {
                return BadRequest("Spielvorschlag existiert bereits");
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
            var spielvorschlag = new Spielvorschlag
            {
                SpielterminId = request.SpielterminId,
                SpielerId = SpielerId,
                SpielvorschlagName = request.SpielvorschlagName
            };
            _context.Spielvorschlaege.Add(spielvorschlag);
            if (await _context.SaveChangesAsync() > 0)
            {
                //return Ok(spielvorschlag);
                return Ok("Spielvorschlag wurde gespeichert");
            }
            return BadRequest("Spielvorschlag konnte nicht gespeichert werden");
        }

        [HttpGet("get-spielvorschlag"), Authorize]
        public async Task<ActionResult<IEnumerable<SpielvorschlagDto>>> GetSpielvorschlag(int spielterminId)
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
            var spielvorschlaege = await _context.Spielvorschlaege.Where(x => x.SpielterminId == spielterminId).ToListAsync();
            var spielvorschlagDtos = spielvorschlaege.Select(x => new SpielvorschlagDto
            {
                SpielvorschlagName = x.SpielvorschlagName,
                SpielerId = x.SpielerId,
                SpielterminId = x.SpielterminId,
                ID = x.ID
            }).ToList();
            return Ok(spielvorschlagDtos);
        }
    }
}
