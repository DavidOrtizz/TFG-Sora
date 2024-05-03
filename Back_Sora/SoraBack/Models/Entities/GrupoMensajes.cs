namespace SoraBack.Models.Entities
{
    public class GrupoMensajes
    {
        public int Id { get; set; }
        public int EmisorId { get; set; }
        public int ReceptorId { get; set; }
        public string ContenidoMensaje { get; set; }

        public MiembroGrupo MiembroGrupo { get; set; }
    }
}
