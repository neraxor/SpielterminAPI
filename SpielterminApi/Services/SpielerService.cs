using SpielterminApi.Models;
using System.Security.Claims;

namespace WebApplication1.Services
{
    public class SpielerService : ISpielerService
    { 
        private readonly IHttpContextAccessor _httpContextAccessor;

        public SpielerService(IHttpContextAccessor httpContextAccessor)
        {
            _httpContextAccessor = httpContextAccessor;
        }

        public int GetSpielerId()
        {
            var result = string.Empty;
            if(_httpContextAccessor.HttpContext is not null)
            {
                result = _httpContextAccessor.HttpContext.User.FindFirst("SpielerId")?.Value;
            }
            if (result is not null && result != string.Empty)
            {
                return int.Parse(result);
            }
            return 0;
        }
    }
}
