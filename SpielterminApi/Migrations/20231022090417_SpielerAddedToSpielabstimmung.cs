using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace SpielterminApi.Migrations
{
    /// <inheritdoc />
    public partial class SpielerAddedToSpielabstimmung : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<int>(
                name: "SpielerId",
                table: "Spielabstimmungen",
                type: "int",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.CreateIndex(
                name: "IX_Spielabstimmungen_SpielerId",
                table: "Spielabstimmungen",
                column: "SpielerId");

            migrationBuilder.AddForeignKey(
                name: "FK_Spielabstimmungen_Spieler_SpielerId",
                table: "Spielabstimmungen",
                column: "SpielerId",
                principalTable: "Spieler",
                principalColumn: "ID",
                onDelete: ReferentialAction.Restrict);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Spielabstimmungen_Spieler_SpielerId",
                table: "Spielabstimmungen");

            migrationBuilder.DropIndex(
                name: "IX_Spielabstimmungen_SpielerId",
                table: "Spielabstimmungen");

            migrationBuilder.DropColumn(
                name: "SpielerId",
                table: "Spielabstimmungen");
        }
    }
}
