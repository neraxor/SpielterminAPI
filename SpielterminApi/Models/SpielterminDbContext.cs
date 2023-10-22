using Microsoft.EntityFrameworkCore;
using System;

namespace SpielterminApi.Models
{
    public class SpielterminDbContext : DbContext
    {

        public SpielterminDbContext(DbContextOptions<SpielterminDbContext> options): base(options)
        {
        }
        public DbSet<Spieler> Spieler { get; set; }
        public DbSet<Spielgruppe> Spielgruppen { get; set; }
        public DbSet<SpielgruppeSpieler> SpielgruppeSpieler { get; set; }
        public DbSet<Spieltermin> Spieltermine { get; set; }
        public DbSet<Spielvorschlag> Spielvorschlaege { get; set; }
        public DbSet<Spielabstimmung> Spielabstimmungen { get; set; }
        public DbSet<Abendbewertung> Abendbewertungen { get; set; }
        public DbSet<Nachricht> Nachrichten { get; set; }
        public DbSet<Essensabstimmung> Essensabstimmungen { get; set; }
        public DbSet<Essensrichtung> Essensrichtungen { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // Deaktivieren des kaskadierenden Löschens für die Beziehung zwischen Spieler und Spielabstimmung
            modelBuilder.Entity<Spielabstimmung>()
                .HasOne(sa => sa.Spieler)
                .WithMany()
                .HasForeignKey(sa => sa.SpielerId)
                .OnDelete(DeleteBehavior.Restrict); // Kaskadierendes Löschen deaktivieren wegen Zirkelreferenz
        }

    }
}
