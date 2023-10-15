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

        public string GetName()
        {
            var result = string.Empty;
            if(_httpContextAccessor.HttpContext is not null)
            {
                //result = _httpContextAccessor.HttpContext.User?.Identity?.Name;
                result = _httpContextAccessor.HttpContext.User.FindFirstValue(ClaimTypes.Name);
                //result = _httpContextAccessor.HttpContext.User.FindAll(ClaimTypes.Role);
            }
            return result;
        }
        public int GetSpielerId()
        {
            var result = string.Empty;
            if(_httpContextAccessor.HttpContext is not null)
            {
                //result = _httpContextAccessor.HttpContext.User?.Identity?.Name;
                result = _httpContextAccessor.HttpContext.User.FindFirst("SpielerId")?.Value;
                //result = _httpContextAccessor.HttpContext.User.FindAll(ClaimTypes.Role);
            }
            if (result is not null && result != string.Empty)
            {
                return int.Parse(result);
            }
            return 0;
        }
    }
}
