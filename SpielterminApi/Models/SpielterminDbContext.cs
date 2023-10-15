using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;

namespace SpielterminApi.Models;

public partial class SpielterminDbContext : DbContext
{
    public SpielterminDbContext()
    {
    }

    public SpielterminDbContext(DbContextOptions<SpielterminDbContext> options)
        : base(options)
    {
    }

    public virtual DbSet<Abendbewertung> Abendbewertungs { get; set; }

    public virtual DbSet<Nachricht> Nachrichts { get; set; }

    public virtual DbSet<Spielabstimmung> Spielabstimmungs { get; set; }

    public virtual DbSet<Spieler> Spielers { get; set; }

    public virtual DbSet<Spielgruppe> Spielgruppes { get; set; }

    public virtual DbSet<Spieltermin> Spieltermins { get; set; }

    public virtual DbSet<Spielvorschlag> Spielvorschlags { get; set; }

//    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
//#warning To protect potentially sensitive information in your connection string, you should move it out of source code. You can avoid scaffolding the connection string by using the Name= syntax to read it from configuration - see https://go.microsoft.com/fwlink/?linkid=2131148. For more guidance on storing connection strings, see http://go.microsoft.com/fwlink/?LinkId=723263.
//        => optionsBuilder.UseSqlServer("Server=localhost;Database=SpielterminDB;Trusted_Connection=True;TrustServerCertificate=True;");

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Abendbewertung>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Abendbew__3214EC2740E28322");

            entity.ToTable("Abendbewertung");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.AbendBewertung1).HasColumnName("AbendBewertung");
            entity.Property(e => e.SpielerId).HasColumnName("SpielerID");
            entity.Property(e => e.SpielterminId).HasColumnName("SpielterminID");

            entity.HasOne(d => d.Spieler).WithMany(p => p.Abendbewertungs)
                .HasForeignKey(d => d.SpielerId)
                .HasConstraintName("FK__Abendbewe__Spiel__4E88ABD4");

            entity.HasOne(d => d.Spieltermin).WithMany(p => p.Abendbewertungs)
                .HasForeignKey(d => d.SpielterminId)
                .HasConstraintName("FK__Abendbewe__Spiel__4D94879B");
        });

        modelBuilder.Entity<Nachricht>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Nachrich__3214EC2740AB4A1D");

            entity.ToTable("Nachricht");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.AbsenderId).HasColumnName("AbsenderID");
            entity.Property(e => e.NachrichtText).IsUnicode(false);
            entity.Property(e => e.SpielgruppeId).HasColumnName("SpielgruppeID");
            entity.Property(e => e.Uhrzeit).HasColumnType("datetime");

            entity.HasOne(d => d.Absender).WithMany(p => p.Nachrichts)
                .HasForeignKey(d => d.AbsenderId)
                .HasConstraintName("FK__Nachricht__Absen__44FF419A");

            entity.HasOne(d => d.Spielgruppe).WithMany(p => p.Nachrichts)
                .HasForeignKey(d => d.SpielgruppeId)
                .HasConstraintName("FK__Nachricht__Spiel__45F365D3");
        });

        modelBuilder.Entity<Spielabstimmung>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Spielabs__3214EC27B38E06F9");

            entity.ToTable("Spielabstimmung");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.SpielerId).HasColumnName("SpielerID");
            entity.Property(e => e.SpielgruppeId).HasColumnName("SpielgruppeID");
            entity.Property(e => e.SpielvorschlagId).HasColumnName("SpielvorschlagID");

            entity.HasOne(d => d.Spieler).WithMany(p => p.Spielabstimmungs)
                .HasForeignKey(d => d.SpielerId)
                .HasConstraintName("FK__Spielabst__Spiel__49C3F6B7");

            entity.HasOne(d => d.Spielgruppe).WithMany(p => p.Spielabstimmungs)
                .HasForeignKey(d => d.SpielgruppeId)
                .HasConstraintName("FK__Spielabst__Spiel__4AB81AF0");

            entity.HasOne(d => d.Spielvorschlag).WithMany(p => p.Spielabstimmungs)
                .HasForeignKey(d => d.SpielvorschlagId)
                .HasConstraintName("FK__Spielabst__Spiel__48CFD27E");
        });

        modelBuilder.Entity<Spieler>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Spieler__3214EC27817A0691");

            entity.ToTable("Spieler");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Benutzername)
                .HasMaxLength(255)
                .IsUnicode(false);
            entity.Property(e => e.Hausnummer)
                .HasMaxLength(10)
                .IsUnicode(false);
            entity.Property(e => e.Nachname)
                .HasMaxLength(255)
                .IsUnicode(false);
            entity.Property(e => e.PasswortHash)
                .HasMaxLength(255)
                .IsUnicode(false);
            entity.Property(e => e.Straße)
                .HasMaxLength(255)
                .IsUnicode(false);
            entity.Property(e => e.Vorname)
                .HasMaxLength(255)
                .IsUnicode(false);
            entity.Property(e => e.Wohnort)
                .HasMaxLength(255)
                .IsUnicode(false);
        });

        modelBuilder.Entity<Spielgruppe>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Spielgru__3214EC27C1A6B1EF");

            entity.ToTable("Spielgruppe");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.SpielerId).HasColumnName("SpielerID");

            entity.HasOne(d => d.Spieler).WithMany(p => p.Spielgruppes)
                .HasForeignKey(d => d.SpielerId)
                .HasConstraintName("FK__Spielgrup__Spiel__398D8EEE");
        });

        modelBuilder.Entity<Spieltermin>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Spielter__3214EC27C665E156");

            entity.ToTable("Spieltermin");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.GastgeberId).HasColumnName("GastgeberID");
            entity.Property(e => e.SpielgruppeId).HasColumnName("SpielgruppeID");
            entity.Property(e => e.Termin).HasColumnType("datetime");

            entity.HasOne(d => d.Gastgeber).WithMany(p => p.Spieltermins)
                .HasForeignKey(d => d.GastgeberId)
                .HasConstraintName("FK__Spielterm__Gastg__3D5E1FD2");

            entity.HasOne(d => d.Spielgruppe).WithMany(p => p.Spieltermins)
                .HasForeignKey(d => d.SpielgruppeId)
                .HasConstraintName("FK__Spielterm__Spiel__3C69FB99");
        });

        modelBuilder.Entity<Spielvorschlag>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Spielvor__3214EC2771F58E3D");

            entity.ToTable("Spielvorschlag");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.SpielerId).HasColumnName("SpielerID");
            entity.Property(e => e.SpielgruppeId).HasColumnName("SpielgruppeID");
            entity.Property(e => e.Spielvorschlag1)
                .IsUnicode(false)
                .HasColumnName("Spielvorschlag");
            entity.Property(e => e.TerminId).HasColumnName("TerminID");

            entity.HasOne(d => d.Spieler).WithMany(p => p.Spielvorschlags)
                .HasForeignKey(d => d.SpielerId)
                .HasConstraintName("FK__Spielvors__Spiel__4222D4EF");

            entity.HasOne(d => d.Spielgruppe).WithMany(p => p.Spielvorschlags)
                .HasForeignKey(d => d.SpielgruppeId)
                .HasConstraintName("FK__Spielvors__Spiel__412EB0B6");

            entity.HasOne(d => d.Termin).WithMany(p => p.Spielvorschlags)
                .HasForeignKey(d => d.TerminId)
                .HasConstraintName("FK__Spielvors__Termi__403A8C7D");
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
