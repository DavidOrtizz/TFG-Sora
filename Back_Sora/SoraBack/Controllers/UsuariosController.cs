using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using Recursos;
using SoraBack.Models;
using SoraBack.Models.Entities;
using System.IdentityModel.Tokens.Jwt;
using System.Security;
using System.Security.Claims;

namespace SoraBack.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsuariosController : ControllerBase
    {
        private readonly DBContext _dbContext;

        // Obtenemos por inyección los parámetros preestablecios para crear los token
        private readonly TokenValidationParameters _tokenParameters;

        public UsuariosController(DBContext dbContext, IOptionsMonitor<JwtBearerOptions> jwtOptions)
        {
            _dbContext = dbContext;
            _tokenParameters = jwtOptions.Get(JwtBearerDefaults.AuthenticationScheme).TokenValidationParameters;
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
            var usuarioEncontrado = _dbContext.IniciarUsuario(usuario.Correo.ToLower(), PasswordHelper.Hash(usuario.Contrasena));

            bool resultado = _dbContext.RegistrarUsuario(new Usuario
            {
                NombreCuenta = usuario.NombreCuenta,
                NombreUsuario = usuario.NombreUsuario,
                Correo = usuario.Correo.ToLower(),
                Contrasena = PasswordHelper.Hash(usuario.Contrasena),
                Descripcion = usuario.Descripcion,
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
            var usuarioEncontrado = _dbContext.IniciarUsuario(usuario.Correo.ToLower(), PasswordHelper.Hash(usuario.Contrasena));

            if (usuarioEncontrado != null)
            {
                var tokenDescriptor = new SecurityTokenDescriptor
                {
                    // Aqui se anade los datos para autorizar al usuario
                    Claims = new Dictionary<string, object>
                    {
                        { "id", usuario.UsuarioId },
                        { ClaimTypes.Role, usuario.Rol}
                    },
                    // Aqui indicamos cuando cuando caduca el token
                    Expires = DateTime.UtcNow.AddDays(30),
                    // Aqui especificamos nuestra clave y el algoritmo de firmado
                    SigningCredentials = new SigningCredentials(
                        _tokenParameters.IssuerSigningKey,
                        SecurityAlgorithms.HmacSha256Signature)
                };
                // Creamos el token y se lo devolvemos al usuario loggeado
                JwtSecurityTokenHandler tokenHandler = new JwtSecurityTokenHandler();
                SecurityToken token = tokenHandler.CreateToken(tokenDescriptor);
                string stringToken = tokenHandler.WriteToken(token);

                // Devolvemos el token y unos datos adicionales
                return Ok(new
                {
                    token = stringToken,
                    nombreUsuario = usuarioEncontrado.NombreUsuario,
                    nombreCuenta = usuarioEncontrado.NombreCuenta,
                    descripcion = usuarioEncontrado.Descripcion
                });
            }

            return Unauthorized();
        }

        [AllowAnonymous]
        [HttpPost("buscarUsuario")]
        public IActionResult BuscarUsuario([FromBody] Usuario usuario)
        {
            // Comprobar si se ha introducido algún dato
            if (usuario == null || string.IsNullOrEmpty(usuario.NombreCuenta))
            {
                return BadRequest(new { mensaje = "No se han proporcionado datos" });
            }

            // Buscar los usuarios que coincidan con las letras propocionadas
            var usuariosEncontrados = _dbContext.Usuarios
                .Where(u => u.NombreCuenta.ToLower().StartsWith(usuario.NombreCuenta.ToLower()))
                .Select(u => new
                {
                    NombreUsuario = u.NombreUsuario,
                    NombreCuenta = u.NombreCuenta
                })
                .ToList();

            // Devuelve la lista de usuarios encontrados
            if (usuariosEncontrados.Any())
            {
                return Ok(usuariosEncontrados);
            }

            return BadRequest(new { mensaje = "No se encontraron usuarios" });
        }

        [AllowAnonymous]
        [HttpGet("obtenerUsuario")]
        public IActionResult ObtenerUsuario([FromBody] Usuario usuario)
        {
            var usuarioEncontrado = _dbContext.EncontrarUsuario(usuario.NombreCuenta.ToLower());
            if (usuarioEncontrado != null)
            {
                return Ok(new
                    {
                        nombreUsuario = usuarioEncontrado.NombreUsuario,
                        nombreCuenta = usuarioEncontrado.NombreCuenta,
                        descripcion = usuarioEncontrado.Descripcion
                    }
                );
            }

            return BadRequest(new { mensaje = "No se ha encontrado el usuario" });
        }
    }
}
