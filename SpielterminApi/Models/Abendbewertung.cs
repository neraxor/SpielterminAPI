using System;
using System.Collections.Generic;

namespace SpielterminApi.Models;

public partial class Abendbewertung
{
    public int Id { get; set; }

    public int? SpielterminId { get; set; }

    public int? SpielerId { get; set; }

    public int? Gastgeberbewertung { get; set; }

    public int? EssenBewertung { get; set; }

    public int? AbendBewertung1 { get; set; }

    public virtual Spieler? Spieler { get; set; }

    public virtual Spieltermin? Spieltermin { get; set; }
}
