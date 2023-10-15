using System;
using System.Collections.Generic;

namespace SpielterminApi.Models;

public partial class Nachricht
{
    public int Id { get; set; }

    public int? AbsenderId { get; set; }

    public int? SpielgruppeId { get; set; }

    public string? NachrichtText { get; set; }

    public DateTime? Uhrzeit { get; set; }

    public virtual Spieler? Absender { get; set; }

    public virtual Spielgruppe? Spielgruppe { get; set; }
}
