namespace SpielterminApi.Models
{
    public class SpielerDto
    {
        public required string Username { get; set; } =string.Empty;
        public required string Password { get; set; } = "$2a$11$KWZCcnaR3ZeUTl/iEfoGWuITJcO/.rFw/kCWenOhwD3H1JwQHN1Ni";
        public string? Vorname { get; set; } = string.Empty;

        public string? Nachname { get; set; } = string.Empty;

        public string? Wohnort { get; set; } = string.Empty;

        public string? Straße { get; set; } = string.Empty;

        public string? Hausnummer { get; set; } = string.Empty;
    }
}
