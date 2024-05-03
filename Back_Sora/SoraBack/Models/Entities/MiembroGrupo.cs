namespace SoraBack.Models.Entities
{
    public class MiembroGrupo
    {
        public int Id { get; set; }
        public int UsuarioId { get; set; }
        public string Rol {  get; set; }

        public Grupo Grupo { get; set; }
        public Usuario Usuario { get; set;}
    }
}
