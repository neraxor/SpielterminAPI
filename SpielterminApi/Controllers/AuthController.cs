﻿using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
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

        [HttpPost("register")]
        public ActionResult<Spieler> Register(SpielerDto request)
        {
            bool userExists = _context.Spielers.Any(s => s.Benutzername == request.Username);
            if (!userExists)
            {
                string passwordHash = BCrypt.Net.BCrypt.HashPassword(request.Password);
                user = new Spieler();
                user.Benutzername = request.Username;
                user.PasswortHash = passwordHash;
                user.Vorname = request.Vorname;
                user.Nachname = request.Nachname;
                user.Wohnort = request.Wohnort;
                user.Straße = request.Straße;
                user.Hausnummer = request.Hausnummer;

                _context.Spielers.Add(user);
                if (_context.SaveChanges() > 0)
                {
                    return Ok(user);
                } 
            }
            else
            {
                return BadRequest("User already exists");
            }           
            return BadRequest("User could not be saved");
            
        } 
        [HttpPost("login")]
        public ActionResult<Spieler> Login(SpielerDto request)
        {
            //db abfrage ob user existiert && halt daten stimmen also user.benutzername hier macht keinen sinn und ist null
            Spieler? user = _context.Spielers.FirstOrDefault(s => s.Benutzername == request.Username);

            if (user is null)
            {
                return BadRequest("User not found.");
            }
            if (!BCrypt.Net.BCrypt.Verify(request.Password, user.PasswortHash))
            {
                return BadRequest("Password is incorrect.");
            }   
            string token = CreateToken(user);
            return Ok(token);
        }
        private string CreateToken(Spieler user)
        {
            List<Claim> claims = new List<Claim>
            {
                //new Claim(ClaimTypes.NameIdentifier,user.Id.ToString()),
                new Claim(ClaimTypes.Name,user.Benutzername),
                new Claim("SpielerId",user.Id.ToString())
            };
            SymmetricSecurityKey key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_config.GetSection("JWT:Token").Value!));
            SigningCredentials cred = new SigningCredentials(key,SecurityAlgorithms.HmacSha512Signature);

            JwtSecurityToken token = new JwtSecurityToken(
                //issuer: "https://localhost:7068/", 
                //audience: "https://localhost:7068/",
                claims: claims,
                expires: DateTime.Now.AddDays(1),
                signingCredentials: cred

                );
                                  
            var jwt = new JwtSecurityTokenHandler().WriteToken(token);

            return jwt;
        }
        [HttpGet,Authorize]
        public ActionResult<string> GetMyName()
        {
            return Ok(_userService.GetName());
        }   
    }
}
