using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace SpielterminApi.Migrations
{
    /// <inheritdoc />
    public partial class NachrichtSpielterminIdAdded : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<int>(
                name: "SpielterminId",
                table: "Nachrichten",
                type: "int",
                nullable: false,
                defaultValue: 0);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "SpielterminId",
                table: "Nachrichten");
        }
    }
}
