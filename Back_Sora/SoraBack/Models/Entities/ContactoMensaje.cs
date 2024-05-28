namespace SoraBack.Models.Entities
{
    public class ContactoMensaje
    {
        public int Id { get; set; }
        public int EmisorId { get; set; }
        public int ReceptorId { get; set; }
        public string ContenidoMensaje { get; set; }

        public SolicitudAmistad Contactos { get; set; }
    }
}
