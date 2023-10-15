using System;
using System.Collections.Generic;

namespace SpielterminApi.Models;

public partial class Spielvorschlag
{
    public int Id { get; set; }

    public int? TerminId { get; set; }

    public int? SpielgruppeId { get; set; }

    public int? SpielerId { get; set; }

    public string? Spielvorschlag1 { get; set; }

    public virtual ICollection<Spielabstimmung> Spielabstimmungs { get; set; } = new List<Spielabstimmung>();

    public virtual Spieler? Spieler { get; set; }

    public virtual Spielgruppe? Spielgruppe { get; set; }

    public virtual Spieltermin? Termin { get; set; }
}
