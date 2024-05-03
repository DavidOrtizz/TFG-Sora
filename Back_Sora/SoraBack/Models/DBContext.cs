﻿using Microsoft.EntityFrameworkCore;
using SoraBack.Models.Entities;

namespace SoraBack.Models
{
    public class DBContext : DbContext
    {
        private const string DATABASE_PATH = "soradb.db";

        // Tablas
        public DbSet<Usuario> Usuarios { get; set; }
        public DbSet<MiembroGrupo> MiembroGrupos { get; set; }
        public DbSet<GrupoMensajes> GrupoMensajes { get; set; }
        public DbSet<Grupo> Grupos {  get; set; } 
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

            Usuarios.Add(usuario);
            guardado = SaveChanges() > 0;

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
