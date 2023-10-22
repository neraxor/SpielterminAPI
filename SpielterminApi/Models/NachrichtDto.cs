namespace SpielterminApi.Models
{
    public class NachrichtDto
    {
        public int ID { get; set; }
        public string NachrichtText { get; set; }
        public DateTime Uhrzeit { get; set; }
        public int AbsenderId { get; set; }
        public int SpielgruppeId { get; set; }
    }
}
