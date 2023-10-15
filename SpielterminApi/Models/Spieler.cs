using System;
using System.Collections.Generic;

namespace SpielterminApi.Models;

public partial class Spieler
{
    public int Id { get; set; }

    public string Benutzername { get; set; } = null!;

    public string PasswortHash { get; set; } = null!;

    public string? Vorname { get; set; }

    public string? Nachname { get; set; }

    public string? Wohnort { get; set; }

    public string? Straße { get; set; }

    public string? Hausnummer { get; set; }

    public virtual ICollection<Abendbewertung> Abendbewertungs { get; set; } = new List<Abendbewertung>();

    public virtual ICollection<Nachricht> Nachrichts { get; set; } = new List<Nachricht>();

    public virtual ICollection<Spielabstimmung> Spielabstimmungs { get; set; } = new List<Spielabstimmung>();

    public virtual ICollection<Spielgruppe> Spielgruppes { get; set; } = new List<Spielgruppe>();

    public virtual ICollection<Spieltermin> Spieltermins { get; set; } = new List<Spieltermin>();

    public virtual ICollection<Spielvorschlag> Spielvorschlags { get; set; } = new List<Spielvorschlag>();
}
