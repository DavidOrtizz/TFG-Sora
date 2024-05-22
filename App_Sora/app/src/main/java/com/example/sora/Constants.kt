package com.example.sora

class Constants {
    companion object {
        const val IP_SERVER = "192.168.1.68:7103"
        const val JWT_KEY = "pqwoifennskunzgvihcaenovscpfoahwegviuaupnzxmjfoaseuh"

        const val URL_REGISTER = "https://" + IP_SERVER + "/api/Usuarios/signup"
        const val URL_LOGIN = "https://" + IP_SERVER + "/api/Usuarios/login"
    }
}