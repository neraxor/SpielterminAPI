using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace SpielterminApi.Models
{
    public class Nachricht
    {
        [Key]
        public int ID { get; set; }
        [Required]
        public string NachrichtText { get; set; }
        [Required]
        public  DateTime Uhrzeit { get; set; }

        [ForeignKey("Spieler")]
        public int AbsenderId { get; set; }

        [ForeignKey("Spielgruppe")]
        public int SpielgruppeId { get; set; }

        [Required]
        public virtual Spieler Absender { get; set; }
        [Required]
        public virtual  Spielgruppe Spielgruppe { get; set; }
    }
}
