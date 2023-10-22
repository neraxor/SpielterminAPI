//using Microsoft.AspNetCore.Authorization;
//using Microsoft.AspNetCore.Http;
//using Microsoft.AspNetCore.Mvc;
//using SpielterminApi.Models;
//using WebApplication1.Services;

//namespace SpielterminApi.Controllers
//{
//    [Route("api/[controller]")]
//    [ApiController]
//    public class SpielvorschlagController : ControllerBase
//    {
//        private readonly SpielterminDbContext _context;
//        private readonly ISpielerService _userService;

//        public SpielvorschlagController(SpielterminDbContext context, ISpielerService userService)
//        {
//            _context = context;
//            _userService = userService;
//        }

//        [HttpPost("create-spielvorschlag"),Authorize]       
//        public async Task<ActionResult<Spielvorschlag>> CreateSpielvorschlag(SpielvorschlagDto request)
//        {
//            var spielvorschlagExists = _context.Spielvorschlaege.Any(s => s.TerminId == request.TerminId && s.Spielvorschlag1 == request.Spielvorschlag1);
//            if (spielvorschlagExists)
//            {
//                return BadRequest("Spielvorschlag already exists");
//            }
//            //todo checken of FK existieren (termin, spielgruppe, spieler)
//            var spielvorschlag = new Spielvorschlag
//            {
//                TerminId = request.TerminId,
//                SpielgruppeId = request.SpielgruppeId,
//                SpielerId = request.SpielerId,
//                Spielvorschlag1 = request.Spielvorschlag1
//            };
//            _context.Spielvorschlaege.Add(spielvorschlag);
//            if (await _context.SaveChangesAsync() > 0)
//            {
//                return Ok(spielvorschlag); 
//            }
//            return BadRequest("Spielvorschlag could not be saved.");
//        }


//    }
//}
