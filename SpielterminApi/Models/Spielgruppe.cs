using System;
using System.Collections.Generic;

namespace SpielterminApi.Models;

public partial class Spielgruppe
{
    public int Id { get; set; }

    public int? SpielerId { get; set; }

    public bool? WarGastgeber { get; set; }

    public virtual ICollection<Nachricht> Nachrichts { get; set; } = new List<Nachricht>();

    public virtual ICollection<Spielabstimmung> Spielabstimmungs { get; set; } = new List<Spielabstimmung>();

    public virtual Spieler? Spieler { get; set; }

    public virtual ICollection<Spieltermin> Spieltermins { get; set; } = new List<Spieltermin>();

    public virtual ICollection<Spielvorschlag> Spielvorschlags { get; set; } = new List<Spielvorschlag>();
}
