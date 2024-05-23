package com.example.sora

class Constants {
    companion object {
        const val IP_SERVER = "192.168.1.68:7103"
        const val JWT_KEY = "pqwoifennskunzgvihcaenovscpfoahwegviuaupnzxmjfoaseuh"

        const val URL_REGISTER = "https://" + IP_SERVER + "/api/Usuarios/signup"
        const val URL_LOGIN = "https://" + IP_SERVER + "/api/Usuarios/login"
        const val URL_BuscarContacto = "https://" + IP_SERVER + "/api/Usuarios/buscarUsuario"
        const val URL_ModificarDatosUsuario = "https://" + IP_SERVER + "/api/Usuarios/modificarDatos"
        const val URL_obtenerDatosUsuario = "https://" + IP_SERVER + "/api/Usuarios/obtenerDatosUsuario"
        const val URL_BuscarGrupo = "https://" + IP_SERVER + "/api/Usuarios/buscarGrupo"
    }
}