using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using SoraBack.Models;

namespace SoraBack.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ContactosController : ControllerBase
    {
        private readonly DBContext _dbContext;

        // Obtenemos por inyección los parámetros preestablecios para crear los token
        private readonly TokenValidationParameters _tokenParameters;

        public ContactosController(DBContext dbContext, IOptionsMonitor<JwtBearerOptions> jwtOptions)
        {
            _dbContext = dbContext;
            _tokenParameters = jwtOptions.Get(JwtBearerDefaults.AuthenticationScheme).TokenValidationParameters;
        }

        [AllowAnonymous]
        [HttpGet("obtenerContactos")]
        public IActionResult ObtenerContactos([FromQuery] string usuario)
        {
            if (string.IsNullOrEmpty(usuario))
            {
                return BadRequest("El usuario no puede estar vacio.");
            }

            var usuarioCuenta = usuario.ToLower();
            Console.WriteLine($"Buscando contactos del usuario: {usuarioCuenta}");

            var contactos = _dbContext.Amistades
                .Where(a => (a.UsuarioEnvia.ToLower() == usuarioCuenta || a.UsuarioRecibe.ToLower() == usuarioCuenta) && a.Estado == "Aceptado")
                .Select(a => new
                {
                    NombreCuenta = a.UsuarioEnvia.ToLower() == usuarioCuenta ? a.UsuarioRecibe : a.UsuarioEnvia,
                    NombreUsuario = _dbContext.Usuarios
                        .Where(u => u.NombreCuenta.ToLower() == (a.UsuarioEnvia.ToLower() == usuarioCuenta ? a.UsuarioRecibe.ToLower() : a.UsuarioEnvia.ToLower()))
                        .Select(u => u.NombreUsuario)
                        .FirstOrDefault()
                })
                .ToList();

            Console.WriteLine($"Contactos encontrados: {contactos.Count}");

            return Ok(contactos);
        }

        [AllowAnonymous]
        [HttpDelete("eliminarContacto")]
        public IActionResult EliminarContacto([FromQuery] string usuario, [FromQuery] string contacto)
        {
            var usuarioCuenta = usuario.ToLower();
            var contactoCuenta = contacto.ToLower();

            var amistad = _dbContext.Amistades.FirstOrDefault(a => (a.UsuarioEnvia.ToLower() == usuarioCuenta && a.UsuarioRecibe.ToLower() == contactoCuenta) || (a.UsuarioEnvia.ToLower() == contactoCuenta && a.UsuarioRecibe.ToLower() == usuarioCuenta));

            _dbContext.Amistades.Remove(amistad);
            _dbContext.SaveChanges();

            return Ok("Contacto eliminado correctamente.");
        }

    }
}
