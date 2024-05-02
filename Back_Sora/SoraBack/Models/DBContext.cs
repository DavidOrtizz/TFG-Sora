using Microsoft.EntityFrameworkCore;
using SoraBack.Models.Entities;

namespace SoraBack.Models
{
    public class DBContext : DbContext
    {
        private const string DATABASE_PATH = "soradb.db";

        public string DbPath { get; set; }

        // Tablas
        public DbSet<Usuario> Usuarios { get; set; }
        public DbSet<MiembroGrupo> MiembroGrupos { get; set; }
        public DbSet<GrupoMensajes> GrupoMensajes { get; set; }
        public DbSet<Grupo> Grupos {  get; set; }
        public DbSet<Contactos> Contactos { get; set; }
        public DbSet<ContactoMensaje> ContactoMensajes { get; set; }

        // Configuración EF para crear un archivo de base de datos
        protected override void OnConfiguring(DbContextOptionsBuilder options)
        {
            string baseDir = AppDomain.CurrentDomain.BaseDirectory;
            options.UseSqlite($"DataSource={baseDir}{DATABASE_PATH}");
        }

        public bool RegistrarUsuario(Usuario usuario)
        {
            bool guardado = false;

            if (Usuarios.FirstOrDefault(usuarioin => usuarioin.Correo.ToLower() == usuario.Correo.ToLower()) == null)
            {
                Usuarios.Add(usuario);
                SaveChanges();
                guardado = true;
            }

            return guardado;
        }

        public Usuario IniciarUsuario(string correo, string contrasena)
        {
            Usuario usuario = Usuarios.FirstOrDefault(usuario => usuario.Correo.ToLower() == correo.ToLower() && usuario.Contrasena == contrasena);

            if (usuario != null)
            {
                return usuario;
            }

            return usuario;
        }
    }
}
