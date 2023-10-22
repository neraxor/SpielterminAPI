using System.ComponentModel.DataAnnotations;

namespace SpielterminApi.Models
{
    public class Essensrichtung
    {
        [Key]
        public int ID { get; set; }
        [Required]
        public string Art { get; set; }
    }
}
