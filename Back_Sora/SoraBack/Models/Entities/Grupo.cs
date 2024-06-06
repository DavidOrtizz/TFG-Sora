namespace SoraBack.Models.Entities
{
    public class Grupo
    {
        public int GrupoId { get; set; }
        public string Nombre { get; set; }
        public string Descripcion { get; set; }
        public List<Usuario> Usuarios { get; set; }
    }
}
