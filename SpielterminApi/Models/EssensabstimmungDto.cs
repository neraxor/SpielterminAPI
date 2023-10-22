namespace SpielterminApi.Models
{
    public class EssensabstimmungDto
    {
        public int ID { get; set; }
        public int EssensrichtungId { get; set; }
        public int SpielerId { get; set; }
        public int SpielterminId { get; set; }
    }
}
