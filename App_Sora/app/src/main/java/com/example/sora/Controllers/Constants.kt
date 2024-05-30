package com.example.sora.Controllers

class Constants {
    companion object {
        const val IP_SERVER = "192.168.1.68:7103"
        const val JWT_KEY = "pqwoifennskunzgvihcaenovscpfoahwegviuaupnzxmjfoaseuh"

        const val URL_REGISTER = "https://" + IP_SERVER + "/api/Usuarios/signup"
        const val URL_LOGIN = "https://" + IP_SERVER + "/api/Usuarios/login"
        const val URL_BuscarUsuario = "https://" + IP_SERVER + "/api/Usuarios/buscarUsuario"
        const val URL_ModificarDatosUsuario = "https://" + IP_SERVER + "/api/Usuarios/modificarDatos"
        const val URL_ModificarIconoPerfil = "https://" + IP_SERVER + "/api/Usuarios/modificarIcono"
        const val URL_EnviarSolicitudAmistad = "https://" + IP_SERVER + "/api/SolicitudAmistad/enviarSolicitudAmistad"
        const val URL_RecibirSolicitudAmistad = "https://" + IP_SERVER + "/api/SolicitudAmistad/recibirSolicitudAmistad"
        const val URL_obtenerDatosUsuario = "https://" + IP_SERVER + "/api/Usuarios/obtenerDatosUsuario"
        const val URL_BuscarGrupo = "https://" + IP_SERVER + "/api/Usuarios/buscarGrupo"
    }
}