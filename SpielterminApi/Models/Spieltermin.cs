using System;
using System.Collections.Generic;

namespace SpielterminApi.Models;

public partial class Spieltermin
{
    public int Id { get; set; }

    public DateTime? Termin { get; set; }

    public int? SpielgruppeId { get; set; }

    public int? GastgeberId { get; set; }

    public virtual ICollection<Abendbewertung> Abendbewertungs { get; set; } = new List<Abendbewertung>();

    public virtual Spieler? Gastgeber { get; set; }

    public virtual Spielgruppe? Spielgruppe { get; set; }

    public virtual ICollection<Spielvorschlag> Spielvorschlags { get; set; } = new List<Spielvorschlag>();
}
