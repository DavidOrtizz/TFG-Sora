package com.example.sora

import com.android.volley.toolbox.HurlStack
import javax.net.ssl.*
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.net.HttpURLConnection
import java.net.URL

class SSLSocketFactoryUtil {
    companion object {
        fun getSSLSocketFactory(): HurlStack {
            return object : HurlStack() {
                override fun createConnection(url: URL?): HttpURLConnection {
                    val connection = super.createConnection(url)
                    if (connection is HttpsURLConnection) {
                        // Crea un TrustManager que no valida los certificados
                        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                                // No hacer nada, no se realiza ninguna verificación del cliente
                            }

                            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                                // No hacer nada, no se realiza ninguna verificación del servidor
                            }

                            override fun getAcceptedIssuers(): Array<X509Certificate> {
                                return arrayOf() // Devuelve una matriz vacía ya que no hay autoridades certificadoras aceptadas
                            }
                        })

                        // Configura el contexto SSL para utilizar el TrustManager personalizado
                        val sslContext = SSLContext.getInstance("TLS")
                        sslContext.init(null, trustAllCerts, SecureRandom())

                        // Asigna el SSLContext al SocketFactory de la conexión HTTPS
                        connection.sslSocketFactory = sslContext.socketFactory

                        // Asigna un HostnameVerifier que siempre devuelve true
                        connection.hostnameVerifier = HostnameVerifier { _, _ -> true }
                    }
                    return connection
                }
            }
        }
    }
}