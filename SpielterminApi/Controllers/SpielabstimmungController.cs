//using Microsoft.AspNetCore.Authorization;
//using Microsoft.AspNetCore.Http;
//using Microsoft.AspNetCore.Mvc;
//using SpielterminApi.Models;
//using WebApplication1.Services;

//namespace SpielterminApi.Controllers
//{
//    [Route("api/[controller]")]
//    [ApiController]
//    public class SpielabstimmungController : ControllerBase
//    {
//        private readonly SpielterminDbContext _context;
//        private readonly ISpielerService _userService;

//        public SpielabstimmungController(SpielterminDbContext context, ISpielerService userService)
//        {
//            _context = context;
//            _userService = userService;
//        }

//        [HttpPost("create-spielabstimmung"), Authorize]
//        public async Task<ActionResult<Spielabstimmung>> CreateSpielabstimmung(SpielabstimmungDto request)
//        {
//            var abstimmungExists = _context.Spielabstimmungen.Any(x=> x.Id == request.SpielvorschlagId && x.SpielvorschlagId == request.SpielerId && x.SpielgruppeId == request.SpielgruppeId);
//            if (abstimmungExists)
//            {
//                return BadRequest("Spielabstimmung already exists");
//            }

//            var spielabstimmung = new Spielabstimmung
//            {
//                SpielvorschlagId = request.SpielvorschlagId,
//                SpielerId = request.SpielerId,
//                Zustimmung = request.Zustimmung,
//                SpielgruppeId = request.SpielgruppeId
//            };
//            //todo checken of FK existieren (spieler, spielgruppe,spielvorschlagid)
//            _context.Spielabstimmungen.Add(spielabstimmung);

//            if (await _context.SaveChangesAsync() > 0)
//            {
//                return Ok(spielabstimmung);
//            }

//            return BadRequest("Spielabstimmung could not be saved.");
//        }
//    }
//}
