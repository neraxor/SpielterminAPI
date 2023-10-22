using System.ComponentModel.DataAnnotations;

namespace SpielterminApi.Models
{
    public class Spielgruppe
    {
        [Key]
        public int ID { get; set; }
        [Required]
        public string Name { get; set; }
        [Required]
        public virtual ICollection<SpielgruppeSpieler> SpielgruppeSpieler { get; set; }

    }
}
