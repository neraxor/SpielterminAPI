namespace SpielterminApi.Models
{
    public class AbendbewertungDto
    {
        public int ID { get; set; }
        public int AbendbewertungName { get; set; }
        public int Essensbewertung { get; set; }
        public int Gastgeberbewertung { get; set; }
        public int SpielerId { get; set; }
        public int SpielterminId { get; set; }
    }
}
