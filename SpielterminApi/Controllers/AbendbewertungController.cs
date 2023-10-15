using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
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

        [HttpPost("create-abendbewertung"), Authorize]
        public async Task<ActionResult<Abendbewertung>> CreateAbendbewertung(AbendbewertungDto request)
        {
            var bewertungExists = _context.Abendbewertungs.Any(x => x.SpielterminId == request.SpielterminId && x.SpielerId == request.SpielerId);
            if (bewertungExists)
            {
                return BadRequest("Abendbewertung existiert bereits");
            }

            var abendbewertung = new Abendbewertung
            {
                SpielterminId = request.SpielterminId,
                SpielerId = request.SpielerId,
                Gastgeberbewertung = request.Gastgeberbewertung,
                EssenBewertung = request.EssenBewertung,
                AbendBewertung1 = request.AbendBewertung1
            };
            // TODO: Überprüfen, ob die Fremdschlüssel (Spieler, Spieltermin) existieren
            _context.Abendbewertungs.Add(abendbewertung);

            if (await _context.SaveChangesAsync() > 0)
            {
                return Ok(abendbewertung);
            }

            return BadRequest("Abendbewertung konnte nicht gespeichert werden.");
        }
    }
}
