namespace SpielterminApi.Models
{
    public class SpielabstimmungDto
    {
        public bool Zustimmung { get; set; }
        public int SpielgruppeId { get; set; }
        public int SpielvorschlagId { get; set; }
        public int SpielerId { get; set; }
    }
}
