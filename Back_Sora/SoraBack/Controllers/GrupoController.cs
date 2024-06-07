using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using SoraBack.Models;
using SoraBack.Models.Entities;

namespace SoraBack.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class GrupoController : Controller
    {
        private readonly DBContext _dbContext;

        public GrupoController(DBContext dbContext)
        {
            _dbContext = dbContext;
        }


        [HttpPost("crearGrupo")]
        public IActionResult CrearGrupo([FromBody] Grupo grupo)
        {
            _dbContext.Grupos.Add(grupo);
            _dbContext.SaveChanges();

            return Ok(new {grupoId = grupo.GrupoId, nombre = grupo.Nombre});
        }


        [HttpGet("obtenerGrupos")]
        public IActionResult ObtenerGrupos()
        {
            var grupos = _dbContext.Grupos.Include(g => g.Usuarios)
                            .Select(g => new { id = g.GrupoId, nombre = g.Nombre })
                            .ToList();
            return Ok(grupos);
        }

        [AllowAnonymous]
        [HttpPost("buscarGrupos")]
        public IActionResult BuscarGrupo([FromBody] Grupo grupo)
        {
            // Comprobar si se ha introducido algún dato
            if (grupo == null || string.IsNullOrEmpty(grupo.Nombre))
            {
                return BadRequest(new { mensaje = "No se han proporcionado datos" });
            }

            // Buscar los grupos que coincidan con las letras propocionadas
            var gruposEncontrados = _dbContext.Grupos
                .Where(u => u.Nombre.ToLower().Contains(grupo.Nombre.ToLower()))
                .Select(u => new
                {
                    Nombre = u.Nombre
                })
                .ToList();

            // Devuelve la lista de grupos encontrados
            if (gruposEncontrados.Any())
            {
                Console.WriteLine($"Grupos encontrados: {gruposEncontrados.Count}");
                return Ok(new { grupos = gruposEncontrados });
            }

            Console.WriteLine("No se encontraron grupos");
            return BadRequest(new { mensaje = "No se encontraron grupos" });
        }

        [AllowAnonymous]
        [HttpPost("unirseGrupo")]
        public IActionResult UnirseGrupo([FromBody] UnirseGrupo unirseGrupo)
        {
            var usuario = _dbContext.Usuarios.Find(unirseGrupo.UsuarioId);
            var grupo = _dbContext.Grupos
                .Include(g => g.Usuarios)
                .FirstOrDefault(g => g.GrupoId == unirseGrupo.GrupoId);

            if (usuario == null || grupo == null)
            {
                return NotFound(new { mensaje = "Usuario o Grupo no encontrado" });
            }

            if (!grupo.Usuarios.Contains(usuario))
            {
                grupo.Usuarios.Add(usuario);
                _dbContext.SaveChanges();
            }

            return Ok(new { mensaje = "El usuario se ha unido al grupo" });
        }
    }
}
