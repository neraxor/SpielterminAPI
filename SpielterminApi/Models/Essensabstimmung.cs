using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace SpielterminApi.Models
{
    public class Essensabstimmung
    {
        [Key]
        public int ID { get; set; }
        [Required]
        public string Essensrichtung { get; set; }

        [ForeignKey("Spieler")]
        public int SpielerId { get; set; }

        [ForeignKey("Spieltermin")]
        public int SpielterminId { get; set; }

        [Required]
        public virtual Spieler Spieler { get; set; }
        [Required]
        public virtual Spieltermin Spieltermin { get; set; }
    }
}
