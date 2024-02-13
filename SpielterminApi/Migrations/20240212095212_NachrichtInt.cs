using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace SpielterminApi.Migrations
{
    /// <inheritdoc />
    public partial class NachrichtInt : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AlterColumn<int>(
                name: "NachrichtText",
                table: "Nachrichten",
                type: "int",
                nullable: false,
                oldClrType: typeof(string),
                oldType: "nvarchar(max)");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AlterColumn<string>(
                name: "NachrichtText",
                table: "Nachrichten",
                type: "nvarchar(max)",
                nullable: false,
                oldClrType: typeof(int),
                oldType: "int");
        }
    }
}
