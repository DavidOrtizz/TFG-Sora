namespace SoraBack.Models.Entities
{
    public class Contactos
    {
        public int ContactosId { get; set; }
        public string Estado { get; set; }

        public Usuario Usuario { get; set;}
    }
}
