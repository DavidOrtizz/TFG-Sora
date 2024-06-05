namespace SoraBack.Models.Entities
{
    public class ContactoMensaje
    {
        public int Id { get; set; }
        public string Emisor { get; set; }
        public string Receptor { get; set; }
        public string Contenido { get; set; }

        public SolicitudAmistad Contactos { get; set; }
    }
}
