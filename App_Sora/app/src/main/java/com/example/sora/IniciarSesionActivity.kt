package com.example.sora

import android.content.Context
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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class IniciarSesionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciar_sesion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intentIniciarSesion = Intent(this, MainActivity::class.java)
        val intentRegistrarse = Intent(this, RegistrarseActivity::class.java)
        val eCorreo = findViewById<EditText>(R.id.textCorreo)
        val eContrasena = findViewById<EditText>(R.id.textContrasena)
        val btnIniciarSesion = findViewById<Button>(R.id.buttonIniciarSesion)
        val btnRegistrarse = findViewById<TextView>(R.id.textViewRegistrarse)
        var modoVista = false

        btnIniciarSesion.setOnClickListener {
            val correo = eCorreo.text.toString()
            val contrasena = eContrasena.text.toString()

            // Poder acceder al interior de la app sin necesidad de que el servidor esté activo.
            // Esto lo he añadido para que no sea necesario descargar y configurar el servidor back
            if(correo.lowercase() == "sora" && contrasena == "1234"){
                Toast.makeText(this, R.string.modoVista, Toast.LENGTH_SHORT).show()
                modoVista = true
                startActivity(intentIniciarSesion)
                finish()
            }

            val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
            val queue = Volley.newRequestQueue(this, sslSocketFactory)

            val jsonBody = JSONObject()
            jsonBody.put("Correo", correo)
            jsonBody.put("Contrasena", contrasena)

            val loginRequest = object : JsonObjectRequest(Request.Method.POST, Constants.URL_LOGIN, jsonBody, Response.Listener {
                response ->
                    val token = response.getString("token")
                    val nombreUsuario = response.getString("nombreUsuario")
                    val nombreCuenta = response.getString("nombreCuenta")
                    val descripcion = response.getString("descripcion")

                    val sharedPreferences = getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)
                    sharedPreferences.edit().apply {
                        putString("token", token)
                        putString("nombreUsuario", nombreUsuario)
                        putString("nombreCuenta", nombreCuenta)
                        putString("descripcion", descripcion)
                        apply()
                    }
                        Toast.makeText(this, R.string.exitoIniciarSesion, Toast.LENGTH_SHORT).show()
                        startActivity(intentIniciarSesion)
                        finish()
                },
                Response.ErrorListener { error ->
                    if (!modoVista){
                        error.printStackTrace()
                        // Manejar errores de la solicitud
                        Toast.makeText(this, R.string.errorInicioSesion, Toast.LENGTH_SHORT).show()
                        Log.d("IniciarSesionActivity", error.toString())
                    }
                }) {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    return headers
                }

            }

            // Agregar la solicitud a la cola
            queue.add(loginRequest)
        }

        // Si no estas registrado que te mande al menu de registrarse
        btnRegistrarse.setOnClickListener {
            startActivity(intentRegistrarse)
            finish()
        }
    }
}