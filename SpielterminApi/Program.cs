using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using SpielterminApi.Models;
using Swashbuckle.AspNetCore.Filters;
using System.Text;
using WebApplication1.Services;
using static System.Net.WebRequestMethods;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddRazorPages();
builder.Services.AddAuthorization();
builder.Services.AddHttpContextAccessor();
builder.Services.AddScoped<ISpielerService, SpielerService>();
builder.Services.AddSwaggerGen(options =>
{
    options.AddSecurityDefinition("oauth2", new OpenApiSecurityScheme
    {
        In = ParameterLocation.Header,
        Name = "Authorization",
        Type = SecuritySchemeType.ApiKey
    });

    options.OperationFilter<SecurityRequirementsOperationFilter>();
}); 
builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
}).AddJwtBearer(options =>
{
    options.TokenValidationParameters = new TokenValidationParameters
    {
        ValidateIssuerSigningKey = true,
        ValidateAudience = false,
        ValidateIssuer = false,
        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(
        builder.Configuration.GetSection("JWT:Token").Value!))
    };
});

builder.Services.AddControllers();
builder.Services.AddDbContext<SpielterminDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("SpielterminDB")));

//für Zugriff aus Android Studio
builder.WebHost.UseUrls("http://0.0.0.0:7063");
//builder.WebHost.UseUrls("https://0.0.0.0:7063");

var app = builder.Build();
app.UseSwaggerUI(c =>
{
    c.SwaggerEndpoint("/swagger/v1/swagger.json", "Spieltermin API v1");
});
// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error");
    app.UseHsts();
}
// Automatische Migration der Datenbank beim starten der Anwendung
using (var scope = app.Services.CreateScope())
{
    var services = scope.ServiceProvider;
    try
    {
        var context = services.GetRequiredService<SpielterminDbContext>();
        context.Database.Migrate();
    }
    catch (Exception ex)
    {
        Console.WriteLine(ex.Message);
    }
}

//app.UseHttpsRedirection();

app.UseStaticFiles();
app.MapControllers();

app.UseRouting();
app.UseAuthentication();
app.UseAuthorization();
app.UseSwagger();

app.MapRazorPages();

app.Run();
