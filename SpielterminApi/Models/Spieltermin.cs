using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace SpielterminApi.Models
{
    public class Spieltermin
    {
        [Key]
        public int ID { get; set; }
        [Required]
        public DateTime Termin { get; set; }

        [ForeignKey("Spielgruppe")]
        public int SpielgruppeId { get; set; }

        [ForeignKey("Spieler")]
        public int GastgeberId { get; set; }

        [Required]
        public virtual Spielgruppe Spielgruppe { get; set; }
    }
}
