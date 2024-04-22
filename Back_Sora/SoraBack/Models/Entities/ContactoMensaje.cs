namespace SoraBack.Models.Entities
{
    public class ContactoMensaje
    {
        public int MensajeContactoId { get; set; }
        public int EmisorId { get; set; }
        public int ReceptorId { get; set; }
        public string ContenidoMensaje { get; set; }

        public Contactos Contactos { get; set; }
    }
}
