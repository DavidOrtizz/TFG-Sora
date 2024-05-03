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
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

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

                val NombreCuenta = eNombre.text.toString()
                val NombreUsuario = NombreCuenta
                val Correo = eCorreo.text.toString()
                val Contrasena = eContrasena.text.toString()
                val RepiteContrasena = eRepiteContrasena.text.toString()
                val Rol = "USUARIO"

                if (Contrasena == RepiteContrasena) {
                    val ResponseListener = Response.Listener<String> { response ->
                        try {
                            val jsonResponse = JSONObject(response)
                            val success = jsonResponse.getBoolean("success")

                            if (success) {
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, R.string.errorRegistrarse, Toast.LENGTH_LONG).show()
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                    try {
                        val registerRequest = RegisterRequest(NombreCuenta, NombreUsuario, Correo, Contrasena, Rol, ResponseListener)
                        val queue: RequestQueue = Volley.newRequestQueue(this)
                        queue.add(registerRequest)
                    } catch (e: Exception) {
                        e.printStackTrace()
                            Toast.makeText(this, "Error en la solicitud de registro", Toast.LENGTH_SHORT).show()
                    }
                } else {
                        Toast.makeText(this, R.string.errorContrasena, Toast.LENGTH_SHORT).show()
                }
            }

        btnIniciar.setOnClickListener {
            startActivity(intent)
            finish()
        }
    }
}