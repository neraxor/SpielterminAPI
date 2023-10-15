using System;
using System.Collections.Generic;

namespace SpielterminApi.Models;

public partial class Spielabstimmung
{
    public int Id { get; set; }

    public int? SpielvorschlagId { get; set; }

    public int? SpielerId { get; set; }

    public bool? Zustimmung { get; set; }

    public int? SpielgruppeId { get; set; }

    public virtual Spieler? Spieler { get; set; }

    public virtual Spielgruppe? Spielgruppe { get; set; }

    public virtual Spielvorschlag? Spielvorschlag { get; set; }
}
