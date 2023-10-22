using System.ComponentModel.DataAnnotations;

namespace SpielterminApi.Models
{
    public class Spieler
    {
        [Key]
        public int ID { get; set; }
        [Required]
        public string Benutzername { get; set; }
        [Required]
        public string PasswordHash { get; set; }
        [Required]
        public string Vorname { get; set; }
        [Required]
        public string Nachname { get; set; }
        [Required]
        public string Wohnort { get; set; }
        [Required]
        public string PLZ { get; set; }
        [Required]
        public string Straße { get; set; }
        [Required]
        public string Hausnummer { get; set; }
        [Required]
        public virtual ICollection<SpielgruppeSpieler> SpielgruppeSpieler { get; set; }

    }
}
