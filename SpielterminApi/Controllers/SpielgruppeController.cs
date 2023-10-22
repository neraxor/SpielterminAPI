using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using SpielterminApi.Models;
using WebApplication1.Services;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;

namespace SpielterminApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SpielgruppeController : ControllerBase
    {
        private readonly SpielterminDbContext _context;
        private readonly ISpielerService _userService;

        public SpielgruppeController(SpielterminDbContext context, ISpielerService userService)
        {
            _context = context;
            _userService = userService;
        }
        /// <summary>
        /// Liefert alle Spielgruppen, in denen der aktuelle Spieler Mitglied ist
        /// </summary>
        /// <returns>GruppenId, Gruppenname & spielgruppeSpieler</returns>
        [HttpGet,Authorize]
        public async Task<ActionResult<IEnumerable<Spielgruppe>>> GetSpielgruppen()
        {
            int SpielerId = _userService.GetSpielerId();

            var spielgruppenIds = _context.SpielgruppeSpieler
                .Where(x => x.SpielerId == SpielerId)
                .Select(x => x.SpielgruppeId)
                .ToList();

            var spielgruppen = await _context.Spielgruppen
                .Where(x => spielgruppenIds.Contains(x.ID))
                .ToListAsync();

            return spielgruppen;
        }

        /// <summary>
        /// Erstellt neue Spielgruppen und fügt den aktuellen Spieler als Mitglied hinzu
        /// </summary>
        /// <param name="spielgruppeDto">benötigt einen Gruppennamen</param>
        /// <returns></returns>
        [HttpPost, Authorize]
        public async Task<ActionResult<SpielgruppeDto>> CreateSpielgruppe(SpielgruppeDto spielgruppeDto)
        {
            var spielgruppe = new Spielgruppe
            {
                Name = spielgruppeDto.Name
            };
            _context.Spielgruppen.Add(spielgruppe);
            await _context.SaveChangesAsync();

            int SpielerId = _userService.GetSpielerId();
            var spielgruppeSpieler = new SpielgruppeSpieler
            {
                SpielgruppeId = spielgruppe.ID,
                SpielerId = SpielerId,
                WarGastgeber = false
            };
            _context.SpielgruppeSpieler.Add(spielgruppeSpieler);
            await _context.SaveChangesAsync();

            var resultDto = new SpielgruppeDto
            {
                Name = spielgruppe.Name
            };

            return Ok(resultDto);
        }


        /// <summary>
        /// Fügt einen Spieler zu einer Spielgruppe hinzu anhand des Benutzernamens
        /// </summary>
        /// <param name="dto">nur SpielgruppeId relevant</param>
        /// <param name="username">benutzername groß&kleinschreibung egal</param>
        /// <returns></returns>
        [HttpPost("AddSpieler"), Authorize]
        public async Task<ActionResult<SpielgruppeSpielerDto>> AddSpielerToGruppe(SpielgruppeSpielerDto dto, string username)
        {
            var user = _context.Spieler.FirstOrDefault(x => x.Benutzername.ToLower() == username.ToLower());
            if (user == null)
            {
                return BadRequest("User not found.");
            }
            var spielgruppeSpieler = new SpielgruppeSpieler
            {
                SpielgruppeId = dto.SpielgruppeId,
                SpielerId = user.ID,
                WarGastgeber = false
            };
            _context.SpielgruppeSpieler.Add(spielgruppeSpieler);
            await _context.SaveChangesAsync();

            var responseDto = new SpielgruppeSpielerDto
            {
                SpielgruppeId = spielgruppeSpieler.SpielgruppeId,
                SpielerId = spielgruppeSpieler.SpielerId,
                WarGastgeber = spielgruppeSpieler.WarGastgeber
            };
            //todo muss ich hier responsen? dto muss sein weil sonst cycle
            return Ok(responseDto);
        }

    }
}
