using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace SpielterminApi.Migrations
{
    /// <inheritdoc />
    public partial class FixedMigration : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "SpielterminId",
                table: "Nachrichten");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<int>(
                name: "SpielterminId",
                table: "Nachrichten",
                type: "int",
                nullable: false,
                defaultValue: 0);
        }
    }
}
