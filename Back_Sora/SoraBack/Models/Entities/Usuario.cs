using Microsoft.EntityFrameworkCore;

namespace SoraBack.Models.Entities
{
    [Index(nameof(Correo), IsUnique = true)]
    public class Usuario
    {
        public int UsuarioId { get; set; }
        public string NombreCuenta { get; set; }
        public string NombreUsuario { get; set; }
        public string Correo { get; set; }
        public string Contrasena { get; set; }
        public string Descripcion { get; set; }
        public string Rol {  get; set; }

    }
}
