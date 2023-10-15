using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using SpielterminApi.Dtos;
using SpielterminApi.Models;
using System.Linq;
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

        [HttpGet,Authorize]
        public async Task<ActionResult<IEnumerable<Spieltermin>>> GetSpieltermine()
        {
            int SpielerId = _userService.GetSpielerId();
            var Spielgruppen = await _context.Spielgruppes
                .Where (x => x.SpielerId == SpielerId)
                .Select(x => x.Id)
                .ToListAsync();

            var spieltermine = await _context.Spieltermins
                                 .Where(x =>x.SpielgruppeId.HasValue && Spielgruppen.Contains(x.SpielgruppeId.Value) && x.Termin > DateTime.Now)
                                 .OrderBy(x => x.Termin)
                                 .ToListAsync();
            return spieltermine;
            //var spieltermine = await _context.Spieltermins.Where(x => x.Spielgruppe =)
        }

        [HttpPost("create-spieltermin"), Authorize]
        public async Task<ActionResult<Spieltermin>> CreateSpieltermin(SpielterminDto request)
        {
            //var spielterminExists = _context.Spieltermins.Any(st => st.Termin == request.Termin && st.SpielgruppeId == request.SpielgruppeId);
            var spielterminExists = _context.Spieltermins.Any(st => st.Termin.HasValue && st.Termin.Value.Date == request.Termin.Value.Date && st.SpielgruppeId == request.SpielgruppeId);

            if (spielterminExists)
            {
                return BadRequest("Spieltermin existiert bereits für diese Spielgruppe und diesen Tag.");
            }

            var spieltermin = new Spieltermin
            {
                Termin = request.Termin ?? DateTime.Now, // Wenn kein Termin angegeben wird, setzen wir standardmäßig die aktuelle Uhrzeit
                SpielgruppeId = request.SpielgruppeId,
                GastgeberId = request.GastgeberId
            };

            _context.Spieltermins.Add(spieltermin);

            if (await _context.SaveChangesAsync() > 0)
            {
                return Ok(spieltermin);
            }

            return BadRequest("Spieltermin konnte nicht gespeichert werden.");
        }

    }
}
