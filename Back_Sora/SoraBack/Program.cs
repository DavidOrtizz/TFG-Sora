
using SoraBack.Models;

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
