using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using SpielterminApi.Models;

namespace SpielterminApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SpielterminController : ControllerBase
    {
        private readonly SpielterminDbContext _context;
        public SpielterminController(SpielterminDbContext context)
        {
            _context = context;
        }

        [HttpGet,Authorize]
        public async Task<ActionResult<IEnumerable<Spieltermin>>> GetSpieltermine()
        {
            return await _context.Spieltermins.ToListAsync();
        }
    }
}
