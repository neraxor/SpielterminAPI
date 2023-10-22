using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace SpielterminApi.Migrations
{
    /// <inheritdoc />
    public partial class Initial : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Essensrichtungen",
                columns: table => new
                {
                    ID = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Art = table.Column<string>(type: "nvarchar(max)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Essensrichtungen", x => x.ID);
                });

            migrationBuilder.CreateTable(
                name: "Spieler",
                columns: table => new
                {
                    ID = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Benutzername = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    PasswordHash = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Vorname = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Nachname = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Wohnort = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    PLZ = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Straße = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Hausnummer = table.Column<string>(type: "nvarchar(max)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Spieler", x => x.ID);
                });

            migrationBuilder.CreateTable(
                name: "Spielgruppen",
                columns: table => new
                {
                    ID = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(type: "nvarchar(max)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Spielgruppen", x => x.ID);
                });

            migrationBuilder.CreateTable(
                name: "Spielvorschlaege",
                columns: table => new
                {
                    ID = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    SpielvorschlagName = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    SpielterminId = table.Column<int>(type: "int", nullable: false),
                    SpielerId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Spielvorschlaege", x => x.ID);
                    table.ForeignKey(
                        name: "FK_Spielvorschlaege_Spieler_SpielerId",
                        column: x => x.SpielerId,
                        principalTable: "Spieler",
                        principalColumn: "ID",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Nachrichten",
                columns: table => new
                {
                    ID = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    NachrichtText = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Uhrzeit = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    AbsenderId = table.Column<int>(type: "int", nullable: false),
                    SpielgruppeId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Nachrichten", x => x.ID);
                    table.ForeignKey(
                        name: "FK_Nachrichten_Spieler_AbsenderId",
                        column: x => x.AbsenderId,
                        principalTable: "Spieler",
                        principalColumn: "ID",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Nachrichten_Spielgruppen_SpielgruppeId",
                        column: x => x.SpielgruppeId,
                        principalTable: "Spielgruppen",
                        principalColumn: "ID",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "SpielgruppeSpieler",
                columns: table => new
                {
                    ID = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    SpielgruppeId = table.Column<int>(type: "int", nullable: false),
                    SpielerId = table.Column<int>(type: "int", nullable: false),
                    WarGastgeber = table.Column<bool>(type: "bit", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_SpielgruppeSpieler", x => x.ID);
                    table.ForeignKey(
                        name: "FK_SpielgruppeSpieler_Spieler_SpielerId",
                        column: x => x.SpielerId,
                        principalTable: "Spieler",
                        principalColumn: "ID",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_SpielgruppeSpieler_Spielgruppen_SpielgruppeId",
                        column: x => x.SpielgruppeId,
                        principalTable: "Spielgruppen",
                        principalColumn: "ID",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Spieltermine",
                columns: table => new
                {
                    ID = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Termin = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    SpielgruppeId = table.Column<int>(type: "int", nullable: false),
                    GastgeberId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Spieltermine", x => x.ID);
                    table.ForeignKey(
                        name: "FK_Spieltermine_Spielgruppen_SpielgruppeId",
                        column: x => x.SpielgruppeId,
                        principalTable: "Spielgruppen",
                        principalColumn: "ID",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Spielabstimmungen",
                columns: table => new
                {
                    ID = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Zustimmung = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    SpielgruppeId = table.Column<int>(type: "int", nullable: false),
                    SpielvorschlagId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Spielabstimmungen", x => x.ID);
                    table.ForeignKey(
                        name: "FK_Spielabstimmungen_Spielgruppen_SpielgruppeId",
                        column: x => x.SpielgruppeId,
                        principalTable: "Spielgruppen",
                        principalColumn: "ID",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Spielabstimmungen_Spielvorschlaege_SpielvorschlagId",
                        column: x => x.SpielvorschlagId,
                        principalTable: "Spielvorschlaege",
                        principalColumn: "ID",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Abendbewertungen",
                columns: table => new
                {
                    ID = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    AbendbewertungName = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Essensbewertung = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Gastgeberbewertung = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    SpielerId = table.Column<int>(type: "int", nullable: false),
                    SpielterminId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Abendbewertungen", x => x.ID);
                    table.ForeignKey(
                        name: "FK_Abendbewertungen_Spieler_SpielerId",
                        column: x => x.SpielerId,
                        principalTable: "Spieler",
                        principalColumn: "ID",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Abendbewertungen_Spieltermine_SpielterminId",
                        column: x => x.SpielterminId,
                        principalTable: "Spieltermine",
                        principalColumn: "ID",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Essensabstimmungen",
                columns: table => new
                {
                    ID = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Essensrichtung = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    SpielerId = table.Column<int>(type: "int", nullable: false),
                    SpielterminId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Essensabstimmungen", x => x.ID);
                    table.ForeignKey(
                        name: "FK_Essensabstimmungen_Spieler_SpielerId",
                        column: x => x.SpielerId,
                        principalTable: "Spieler",
                        principalColumn: "ID",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Essensabstimmungen_Spieltermine_SpielterminId",
                        column: x => x.SpielterminId,
                        principalTable: "Spieltermine",
                        principalColumn: "ID",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_Abendbewertungen_SpielerId",
                table: "Abendbewertungen",
                column: "SpielerId");

            migrationBuilder.CreateIndex(
                name: "IX_Abendbewertungen_SpielterminId",
                table: "Abendbewertungen",
                column: "SpielterminId");

            migrationBuilder.CreateIndex(
                name: "IX_Essensabstimmungen_SpielerId",
                table: "Essensabstimmungen",
                column: "SpielerId");

            migrationBuilder.CreateIndex(
                name: "IX_Essensabstimmungen_SpielterminId",
                table: "Essensabstimmungen",
                column: "SpielterminId");

            migrationBuilder.CreateIndex(
                name: "IX_Nachrichten_AbsenderId",
                table: "Nachrichten",
                column: "AbsenderId");

            migrationBuilder.CreateIndex(
                name: "IX_Nachrichten_SpielgruppeId",
                table: "Nachrichten",
                column: "SpielgruppeId");

            migrationBuilder.CreateIndex(
                name: "IX_Spielabstimmungen_SpielgruppeId",
                table: "Spielabstimmungen",
                column: "SpielgruppeId");

            migrationBuilder.CreateIndex(
                name: "IX_Spielabstimmungen_SpielvorschlagId",
                table: "Spielabstimmungen",
                column: "SpielvorschlagId");

            migrationBuilder.CreateIndex(
                name: "IX_SpielgruppeSpieler_SpielerId",
                table: "SpielgruppeSpieler",
                column: "SpielerId");

            migrationBuilder.CreateIndex(
                name: "IX_SpielgruppeSpieler_SpielgruppeId",
                table: "SpielgruppeSpieler",
                column: "SpielgruppeId");

            migrationBuilder.CreateIndex(
                name: "IX_Spieltermine_SpielgruppeId",
                table: "Spieltermine",
                column: "SpielgruppeId");

            migrationBuilder.CreateIndex(
                name: "IX_Spielvorschlaege_SpielerId",
                table: "Spielvorschlaege",
                column: "SpielerId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Abendbewertungen");

            migrationBuilder.DropTable(
                name: "Essensabstimmungen");

            migrationBuilder.DropTable(
                name: "Essensrichtungen");

            migrationBuilder.DropTable(
                name: "Nachrichten");

            migrationBuilder.DropTable(
                name: "Spielabstimmungen");

            migrationBuilder.DropTable(
                name: "SpielgruppeSpieler");

            migrationBuilder.DropTable(
                name: "Spieltermine");

            migrationBuilder.DropTable(
                name: "Spielvorschlaege");

            migrationBuilder.DropTable(
                name: "Spielgruppen");

            migrationBuilder.DropTable(
                name: "Spieler");
        }
    }
}
