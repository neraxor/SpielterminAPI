namespace SpielterminApi.Models
{
    public class SpielvorschlagDto
    {
        public int? TerminId { get; set; }
        public int? SpielgruppeId { get; set; } = 5;
        public int? SpielerId { get; set; } = 6;
        public string? Spielvorschlag1 { get; set; } = "Spielvorschlag1";
    }

}
