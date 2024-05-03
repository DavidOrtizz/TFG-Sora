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
            return resultado ? Ok() : BadRequest();
        }
    }
}
