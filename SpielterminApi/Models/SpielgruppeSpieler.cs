using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace SpielterminApi.Models
{
    public class SpielgruppeSpieler
    {
        [Key]
        public int ID { get; set; }

        [ForeignKey("Spielgruppe")]
        public int SpielgruppeId { get; set; }

        [ForeignKey("Spieler")]
        public int SpielerId { get; set; }
        [Required]
        public bool WarGastgeber { get; set; }
        [Required]
        public virtual Spieler Spieler { get; set; }
        [Required]
        public virtual Spielgruppe Spielgruppe { get; set; }
    }
}
