namespace SpielterminApi.Models
{
    public class SpielterminDto
    {
        public int ID { get; set; }
        public DateTime Termin { get; set; }
        public int SpielgruppeId { get; set; }
        public int GastgeberId { get; set; }
    }
}
