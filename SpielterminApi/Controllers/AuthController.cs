using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using SpielterminApi.Models;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using WebApplication1.Services;

namespace SpielterminApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        public static Spieler user = new Spieler();
        private readonly IConfiguration _config;
        private readonly ISpielerService _userService;
        private readonly SpielterminDbContext _context;

        public AuthController(IConfiguration configuration, ISpielerService userService, SpielterminDbContext context)
        {
            _config = configuration;
            _userService = userService;
            _context = context;
        }

        [HttpPost("Migration")]
        public ActionResult Migration()
        {
            try
            {
                _context.Database.Migrate();
                return Ok("Migration erfolgreich");
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("register")]
        public ActionResult<Spieler> Register(SpielerDto request)
        {
            bool userExists = _context.Spieler.Any(s => s.Benutzername == request.Username);
            if (!userExists)
            {
                string passwordHash = BCrypt.Net.BCrypt.HashPassword(request.Password);
                user = new Spieler
                {
                    Benutzername = request.Username,
                    PasswordHash = passwordHash,
                    Vorname = request.Vorname,
                    Nachname = request.Nachname,
                    Wohnort = request.Wohnort,
                    Straße = request.Straße,
                    Hausnummer = request.Hausnummer,
                    PLZ = request.PLZ
                };

                _context.Spieler.Add(user);
                if (_context.SaveChanges() > 0)
                {
                    var userResponse = new SpielerDto
                    {
                        Username = user.Benutzername,
                        Vorname = user.Vorname,
                        Nachname = user.Nachname,
                        Wohnort = user.Wohnort,
                        Straße = user.Straße,
                        Hausnummer = user.Hausnummer,
                        PLZ = user.PLZ
                    };
                    return Ok(userResponse);
                }
            }
            else
            {
                return BadRequest("Benutzer existiert bereits");
            }
            return BadRequest("Benutzer konnte nicht gespeichert werden");

        }
        [HttpPost("login")]
        public ActionResult<Spieler> Login(SpielerDto request)
        {
            Spieler? user = _context.Spieler.FirstOrDefault(s => s.Benutzername == request.Username);

            if (user is null)
            {
                return BadRequest("Benutzer wurde nicht gefunden");
            }
            if (!BCrypt.Net.BCrypt.Verify(request.Password, user.PasswordHash))
            {
                return BadRequest("Passwort ist falsch");
            }
            string token = CreateToken(user);
            return Ok(token);
        }
        private string CreateToken(Spieler user)
        {
            List<Claim> claims = new List<Claim>
            {
                new Claim(ClaimTypes.Name,user.Benutzername),
                new Claim("SpielerId",user.ID.ToString())
            };
            SymmetricSecurityKey key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_config.GetSection("JWT:Token").Value!));
            SigningCredentials cred = new SigningCredentials(key, SecurityAlgorithms.HmacSha512Signature);

            JwtSecurityToken token = new JwtSecurityToken(
                claims: claims,
                expires: DateTime.Now.AddDays(1),
                signingCredentials: cred

                );

            var jwt = new JwtSecurityTokenHandler().WriteToken(token);

            return jwt;
        }
    }
}
