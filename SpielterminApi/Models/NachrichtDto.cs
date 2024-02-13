namespace SpielterminApi.Models
{
    public class NachrichtDto
    {
        public int ID { get; set; }
        public int NachrichtText { get; set; }
        public DateTime Uhrzeit { get; set; }
        public int AbsenderId { get; set; }
        public int SpielgruppeId { get; set; }
        public int SpielterminId { get; set; }
    }
}
