package com.example.sora

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class RegistrarseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                enableEdgeToEdge()
        setContentView(R.layout.activity_registrarse)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = Intent(this, IniciarSesionActivity::class.java)

        val eNombre = findViewById<EditText>(R.id.textNombreCuenta)
        val eCorreo = findViewById<EditText>(R.id.textCorreo)
        val eContrasena = findViewById<EditText>(R.id.textContrasena)
        val eRepiteContrasena = findViewById<EditText>(R.id.textRepiteContrasena)

        val btnRegistrarse = findViewById<Button>(R.id.buttonRegistrarse)
        val btnIniciar = findViewById<TextView>(R.id.textViewIniciar)

        btnRegistrarse.setOnClickListener {
            Log.d("RegistrarseActivity", "Se hizo clic en el botón de registro")

            val nombreCuenta = eNombre.text.toString()
            val nombreUsuario = nombreCuenta
            val correo = eCorreo.text.toString()
            val contrasena = eContrasena.text.toString()
            val repiteContrasena = eRepiteContrasena.text.toString()
            val rol = "USUARIO"

            Log.d("RegistrarseActivity", "Datos: Nombre de cuenta: $nombreCuenta, Correo: $correo, Contraseña: $contrasena, Repite Contraseña: $repiteContrasena")

            if (!nombreCuenta.isNullOrBlank()){
                if (!correo.isNullOrBlank()){
                    if (!contrasena.isNullOrBlank()){
                        // Verificar que las contraseñas coincidan
                        if (contrasena == repiteContrasena) {
                            // Crear una instancia de RequestQueue
                            val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
                            val queue = Volley.newRequestQueue(this, sslSocketFactory)

                            // Crear el objeto JSON con los datos del usuario
                            val jsonObject = JSONObject()
                            jsonObject.put("NombreCuenta", nombreCuenta)
                            jsonObject.put("NombreUsuario", nombreUsuario)
                            jsonObject.put("Correo", correo)
                            jsonObject.put("Contrasena", contrasena)
                            jsonObject.put("Rol", rol)

                            // Crear la solicitud de registro
                            val registerRequest = object : StringRequest(Request.Method.POST, Constants.URL_REGISTER, Response.Listener {
                                response ->
                                    try {
                                        Log.d("RegistrarseActivity", "Respuesta del servidor: $response")

                                        // Procesar la respuesta del servidor
                                        val jsonResponse = JSONObject(response)
                                        val mensaje = jsonResponse.getString("mensaje")

                                        Log.d("RegistrarseActivity", "Mensaje recibido: $mensaje")

                                        if (mensaje == "Usuario registrado correctamente") {
                                            // Registro exitoso
                                            Toast.makeText(this, R.string.exitoRegistro, Toast.LENGTH_SHORT).show()
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            // Registro fallido
                                            Toast.makeText(this, R.string.errorRegistrarse, Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                },
                                Response.ErrorListener { error ->
                                    error.printStackTrace()
                                    // Manejar errores de la solicitud
                                    Toast.makeText(this, "Error en la solicitud: " + error.message, Toast.LENGTH_SHORT).show()
                                    Log.d("RegistrarseActivity", error.toString())
                                }) {
                                override fun getBody(): ByteArray {
                                    // Obtener el cuerpo de la solicitud como un array de bytes
                                    return jsonObject.toString().toByteArray()
                                }

                                override fun getBodyContentType(): String {
                                    return "application/json; charset=utf-8"
                                }
                            }

                                // Agregar la solicitud a la cola de solicitudes
                                queue.add(registerRequest)

                        } else {
                            // Las contraseñas no coinciden
                            Toast.makeText(this, R.string.errorContrasena, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Aviso por contraseña
                        Toast.makeText(this, R.string.introduceContrasena, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Aviso por correo
                    Toast.makeText(this, R.string.introduceCorreo, Toast.LENGTH_SHORT).show()
                }
            } else {
                // Aviso por nombre usuario
                Toast.makeText(this, R.string.introduceNombre, Toast.LENGTH_SHORT).show()
            }
        }


        btnIniciar.setOnClickListener {
            startActivity(intent)
            finish()
        }
    }
}