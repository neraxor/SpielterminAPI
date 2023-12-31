﻿using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace SpielterminApi.Models
{
    public class Spielvorschlag
    {
        [Key]
        public int ID { get; set; }
        [Required]
        public string SpielvorschlagName { get; set; }

        [ForeignKey("Spieltermin")]
        public int SpielterminId { get; set; }

        [ForeignKey("Spieler")]
        public int SpielerId { get; set; }
        [Required]
        public virtual Spieler Spieler { get; set; }
    }
}
