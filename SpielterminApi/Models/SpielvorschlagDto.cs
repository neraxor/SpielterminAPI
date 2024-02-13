namespace SpielterminApi.Models
{
    public class SpielvorschlagDto
    {
        public int ID { get; set; }
        public string SpielvorschlagName { get; set; }
        public int SpielterminId { get; set; }
        public int SpielerId { get; set; }
    }
}
