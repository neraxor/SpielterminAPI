using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using SpielterminApi.Models;

namespace SpielterminApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class EssensrichtungController : ControllerBase
    {
        private readonly SpielterminDbContext _context;
        public EssensrichtungController(SpielterminDbContext context)
        {
            _context = context;
        }

        /// <summary>
        /// Gibt alle Essensrichtungen zurück
        /// </summary>
        /// <returns></returns>
        [HttpGet,Authorize]
        public async Task<ActionResult<IEnumerable<Essensrichtung>>> GetAllEssensrichtungen()
        {
            return await _context.Essensrichtungen.ToListAsync();
        }
    }
}
