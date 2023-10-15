using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
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

        [HttpPost("create-nachricht"), Authorize]
        public async Task<ActionResult<Nachricht>> CreateNachricht(NachrichtDto request)
        {
            var nachrichtExists = _context.Nachrichts.Any(n => n.AbsenderId == request.AbsenderId && n.SpielgruppeId == request.SpielgruppeId && n.NachrichtText == request.NachrichtText);

            if (nachrichtExists)
            {
                return BadRequest("Nachricht existiert bereits");
            }

            var nachricht = new Nachricht
            {
                AbsenderId = request.AbsenderId,
                SpielgruppeId = request.SpielgruppeId,
                NachrichtText = request.NachrichtText,
                Uhrzeit =  DateTime.Now // Standardmäßig auf aktuelle Uhrzeit setzen, wenn keine Uhrzeit angegeben ist
                //Uhrzeit = request.Uhrzeit ?? DateTime.Now // Standardmäßig auf aktuelle Uhrzeit setzen, wenn keine Uhrzeit angegeben ist
            };
            // TODO: Überprüfen, ob FK existieren (Absender, Spielgruppe)
            _context.Nachrichts.Add(nachricht);

            if (await _context.SaveChangesAsync() > 0)
            {
                return Ok(nachricht);
            }

            return BadRequest("Nachricht konnte nicht gespeichert werden.");
        }
    }
}
