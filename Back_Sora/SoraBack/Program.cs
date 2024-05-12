
using Microsoft.IdentityModel.Tokens;
using SoraBack.Models;
using System.Text;

namespace SoraBack
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);

            // Add services to the container.

            builder.Services.AddControllers();
            // Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
            builder.Services.AddEndpointsApiExplorer();
            builder.Services.AddSwaggerGen();
            builder.Services.AddScoped<DBContext>();

            builder.Services.AddAuthentication()
                .AddJwtBearer(options =>
                {
                    // Por seguridad guardamos la clave privada en variables de entorno
                    string key = Environment.GetEnvironmentVariable("JWT_KEY");

                    options.TokenValidationParameters = new TokenValidationParameters()
                    {
                        // Que se valide o no el emisor del token
                        ValidateIssuer = false,
                        // Que se valide para quién o para que propósito está destinado el token
                        ValidateAudience = false,
                        // Indicamos la clave
                        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(key))
                    };
                });

            var app = builder.Build();

            using (IServiceScope scope = app.Services.CreateScope())
            {
                DBContext dBContext = scope.ServiceProvider.GetService<DBContext>();
                dBContext.Database.EnsureCreated();
            }

                // Configure the HTTP request pipeline.
                if (app.Environment.IsDevelopment())
                {
                    app.UseSwagger();
                    app.UseSwaggerUI();
                }

            app.UseHttpsRedirection();

            // Habilita la autenticación
            app.UseAuthentication();
            // Habilita la autorización
            app.UseAuthorization();

            app.MapControllers();

            app.Run();
        }
    }
}
