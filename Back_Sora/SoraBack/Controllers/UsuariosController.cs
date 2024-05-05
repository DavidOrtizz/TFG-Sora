using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using SoraBack.Models;
using SoraBack.Models.Entities;

namespace SoraBack.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsuariosController : ControllerBase
    {
        private readonly DBContext _dbContext;

        public UsuariosController(DBContext dbContext)
        {
            _dbContext = dbContext;
        }


        [HttpGet]
        public IActionResult Get()
        {
                var usuarios = _dbContext.Usuarios.ToList();
                return Ok(usuarios);
        }

        [AllowAnonymous]
        [HttpPost("signup")]
        public IActionResult RegistrarUsuario([FromBody] Usuario usuario)
        {
            bool resultado = _dbContext.RegistrarUsuario(new Usuario
            {
                NombreCuenta = usuario.NombreCuenta,
                NombreUsuario = usuario.NombreUsuario,
                Correo = usuario.Correo.ToLower(),
                Contrasena = usuario.Contrasena,
                Rol = "USUARIO"
            });

            if (resultado)
            {
                return Ok(new { mensaje = "Usuario registrado correctamente" });
            }
            else
            {
                return BadRequest(new { mensaje = "Error al registrar usuario" });
            }
        }

        [AllowAnonymous]
        [HttpPost("login")]
        public IActionResult IniciarSesion([FromBody] Usuario usuario)
        {
            // Comprueba si el usuario existe en la base de datos
            var usuarioEncontrado = _dbContext.Usuarios.FirstOrDefault(u => u.Correo.ToLower() == usuario.Correo.ToLower());

            if (usuarioEncontrado != null && usuarioEncontrado.Contrasena == usuario.Contrasena)
            {
                return Ok(new { mensaje = "Inicio de sesión" });
            }
            else
            {
                return BadRequest(new { mensaje = "Error al iniciar sesión" });
            }
        }
    }
}
