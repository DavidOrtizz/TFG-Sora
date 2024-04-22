namespace SoraBack.Models.Entities
{
    public class Usuario
    {
        public int UsuarioId { get; set; }
        public string NombreCuenta { get; set; }
        public string NombreUsuario { get; set; }
        public string Correo { get; set; }
        public string Contrasena { get; set; }
        public string descripcion { get; set; }

    }
}
