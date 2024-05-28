namespace SoraBack.Models.Entities
{
    public class SolicitudAmistad
    {
        public int Id { get; set; }
        public string UsuarioEnvia { get; set; }
        public string UsuarioRecibe { get; set; }
        public string Estado { get; set; } // Pendiente, Aceptada, Rechazada

        public Usuario Usuario { get; set;}
    }
}
