using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace SpielterminApi.Migrations
{
    /// <inheritdoc />
    public partial class EssensabstimmungFK : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Essensrichtung",
                table: "Essensabstimmungen");

            migrationBuilder.AddColumn<int>(
                name: "EssensrichtungId",
                table: "Essensabstimmungen",
                type: "int",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.CreateIndex(
                name: "IX_Essensabstimmungen_EssensrichtungId",
                table: "Essensabstimmungen",
                column: "EssensrichtungId");

            migrationBuilder.AddForeignKey(
                name: "FK_Essensabstimmungen_Essensrichtungen_EssensrichtungId",
                table: "Essensabstimmungen",
                column: "EssensrichtungId",
                principalTable: "Essensrichtungen",
                principalColumn: "ID",
                onDelete: ReferentialAction.Cascade);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Essensabstimmungen_Essensrichtungen_EssensrichtungId",
                table: "Essensabstimmungen");

            migrationBuilder.DropIndex(
                name: "IX_Essensabstimmungen_EssensrichtungId",
                table: "Essensabstimmungen");

            migrationBuilder.DropColumn(
                name: "EssensrichtungId",
                table: "Essensabstimmungen");

            migrationBuilder.AddColumn<string>(
                name: "Essensrichtung",
                table: "Essensabstimmungen",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");
        }
    }
}
