using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using SoraBack.Models;
using SoraBack.Models.Entities;

namespace SoraBack.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SolicitudAmistadController : ControllerBase
    {
        private readonly DBContext _dbContext;

        // Obtenemos por inyección los parámetros preestablecios para crear los token
        private readonly TokenValidationParameters _tokenParameters;

        public SolicitudAmistadController(DBContext dbContext, IOptionsMonitor<JwtBearerOptions> jwtOptions)
        {
            _dbContext = dbContext;
            _tokenParameters = jwtOptions.Get(JwtBearerDefaults.AuthenticationScheme).TokenValidationParameters;
        }

        [AllowAnonymous]
        [HttpPost("enviarSolicitudAmistad")]
        public IActionResult EnviarSolicitudAmistad([FromBody] SolicitudAmistad solicitudAmistad)
        {
            // Busca el usuario que envía la solicitud
            var usuarioBusca = _dbContext.Usuarios.FirstOrDefault(u => u.NombreCuenta.ToLower() == solicitudAmistad.UsuarioEnvia.ToLower());
            // Busca el usuario que recibe la solicitud
            var usuarioRecibe = _dbContext.Usuarios.FirstOrDefault(u => u.NombreCuenta.ToLower() == solicitudAmistad.UsuarioRecibe.ToLower());

            // Crear una nueva solicitud de amistad
            var nuevaSolicitud = new SolicitudAmistad
            {
                UsuarioEnvia = usuarioBusca.NombreCuenta,
                UsuarioRecibe = usuarioRecibe.NombreCuenta,
                Estado = "Pendiente"
            };

            _dbContext.Amistades.Add(nuevaSolicitud);
            _dbContext.SaveChanges();

            return Ok(new { mensaje = "Solicitud de amistad enviada" });
        }

        [AllowAnonymous]
        [HttpGet("recibirSolicitudAmistad")]
        public IActionResult RecibirSolicitudAmistad([FromQuery] string usuarioRecibe)
        {
            // Se obtiene el nombre de cuenta del usuario que ha iniciado sesión
            var nombreCuenta = usuarioRecibe.ToLower();

            var solicitudes = _dbContext.Amistades
                .Where(s => s.UsuarioRecibe.ToLower() == nombreCuenta && s.Estado == "Pendiente")
                .Select(s => new
                {
                    s.Id,
                    s.UsuarioEnvia,
                    s.UsuarioRecibe,
                    s.Estado
                })
                .ToList();

            return Ok(solicitudes);
        }

        [AllowAnonymous]
        [HttpPost("aceptarSolicitudAmistad")]
        public IActionResult AceptarSolicitudAmistad([FromQuery] string usuarioEnvia, [FromQuery] string usuarioRecibe)
        {
            var solicitud = _dbContext.Amistades.FirstOrDefault(s => s.UsuarioEnvia.ToLower() == usuarioEnvia.ToLower() && s.UsuarioRecibe.ToLower() == usuarioRecibe.ToLower() && s.Estado == "Pendiente");

            if (solicitud == null)
            {
                return NotFound("Solicitud de amistad no encontrada");
            }

            solicitud.Estado = "Aceptado";
            _dbContext.SaveChanges();

            return Ok(new { mensaje = "Solicitud de amistad aceptada" });
        }

        [AllowAnonymous]
        [HttpPost("rechazarSolicitudAmistad")]
        public IActionResult RechazarSolicitudAmistad([FromQuery] string usuarioEnvia, [FromQuery] string usuarioRecibe)
        {
            var solicitud = _dbContext.Amistades.FirstOrDefault(s => s.UsuarioEnvia.ToLower() == usuarioEnvia.ToLower() && s.UsuarioRecibe.ToLower() == usuarioRecibe.ToLower() && s.Estado == "Pendiente");

            if (solicitud == null)
            {
                return NotFound("Solicitud de amistad no encontrada");
            }

            _dbContext.Amistades.Remove(solicitud);
            _dbContext.SaveChanges();

            return Ok(new { mensaje = "Solicitud de amistad rechazada" });
        }
    }
}

