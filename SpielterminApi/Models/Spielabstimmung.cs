using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace SpielterminApi.Models
{
    public class Spielabstimmung
    {
        [Key]
        public int ID { get; set; }
        [Required]
        public bool Zustimmung { get; set; }

        [ForeignKey("Spielgruppe")]
        public int SpielgruppeId { get; set; }

        [ForeignKey("Spielvorschlag")]
        public int SpielvorschlagId { get; set; }

        [ForeignKey("Spieler")]
        public int SpielerId { get; set; }

        [Required]
        public virtual Spielgruppe Spielgruppe { get; set; }
        [Required]
        public virtual Spielvorschlag Spielvorschlag { get; set; }
        [Required]
        public virtual Spieler Spieler { get; set; }
    }
}
