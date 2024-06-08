package com.example.sora.Controllers

class Constants {
    companion object {
        const val IP_SERVER = "192.168.1.68:7103"
        const val JWT_KEY = "pqwoifennskunzgvihcaenovscpfoahwegviuaupnzxmjfoaseuh"

        // Registro e inicio de sesión
        const val URL_REGISTER = "https://" + IP_SERVER + "/api/Usuarios/signup"
        const val URL_LOGIN = "https://" + IP_SERVER + "/api/Usuarios/login"

        // Modificar datos
        const val URL_ModificarDatosUsuario = "https://" + IP_SERVER + "/api/Usuarios/modificarDatos"
        const val URL_ModificarIconoPerfil = "https://" + IP_SERVER + "/api/Usuarios/modificarIcono"

        // Gestion solicitud de amistad
        const val URL_BuscarUsuario = "https://" + IP_SERVER + "/api/Usuarios/buscarUsuario"
        const val URL_EnviarSolicitudAmistad = "https://" + IP_SERVER + "/api/SolicitudAmistad/enviarSolicitudAmistad"
        const val URL_RecibirSolicitudAmistad = "https://" + IP_SERVER + "/api/SolicitudAmistad/recibirSolicitudAmistad"
        const val URL_AceptarSolicitudAmistad = "https://" + IP_SERVER + "/api/SolicitudAmistad/aceptarSolicitudAmistad"
        const val URL_RechazarSolicitudAmistad = "https://" + IP_SERVER + "/api/SolicitudAmistad/rechazarSolicitudAmistad"

        // Gestion Contactos
        const val URL_ObtenerContactos = "https://" + IP_SERVER + "/api/Contactos/obtenerContactos"
        const val URL_EliminarContactos = "https://" + IP_SERVER + "/api/Contactos/eliminarContacto"

        // Gestion grupos
        const val URL_CrearGrupo = "https://" + IP_SERVER + "/api/Grupo/crearGrupo"
        const val URL_ObtenerGrupos = "https://" + IP_SERVER + "/api/Grupo/obtenerGrupos"
        const val URL_BuscarGrupos = "https://" + IP_SERVER + "/api/Grupo/buscarGrupos"
        const val URL_UnirseGrupo = "https://" + IP_SERVER + "/api/Grupo/unirseGrupo"
        const val URL_MostrarGruposUnidos = "https://" + IP_SERVER + "/api/Grupo/mostrarGruposUnidos/"

        // Sin usar...
        const val URL_obtenerDatosUsuario = "https://" + IP_SERVER + "/api/Usuarios/obtenerDatosUsuario"
        const val URL_BuscarGrupo = "https://" + IP_SERVER + "/api/Usuarios/buscarGrupo"
    }
}