package com.example.sora.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.sora.Adapter.ContactAdapter
import com.example.sora.Controllers.Constants
import com.example.sora.Controllers.SSLSocketFactoryUtil
import com.example.sora.R
import com.example.sora.Datos.UsuarioResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class MenuAgregarContacto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_agregar_contacto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnVolver = findViewById<ImageButton>(R.id.buttonVolver)
        val intentVolver = Intent(this, MainActivity::class.java)
            .putExtra("cargarMenu","Contactos")

        btnVolver.setOnClickListener {
            startActivity(intentVolver)
            finish()
        }

        val textBusqueda = findViewById<EditText>(R.id.EditBuscador)
        val recyclerView : RecyclerView = findViewById(R.id.contactosEncontrados)
        recyclerView.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.IO).launch {
            textBusqueda.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrEmpty()) {
                        buscarContacto(s.toString())
                    }
                }
            })
        }
    }

    private fun buscarContacto(nombreCuenta: String) {
        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(this, sslSocketFactory)
        val recyclerView : RecyclerView = findViewById(R.id.contactosEncontrados)

        val jsonObject = JSONObject().apply {
            put("NombreCuenta", nombreCuenta)
        }

        val request = object : JsonObjectRequest(Method.POST, Constants.URL_BuscarUsuario, jsonObject, Response.Listener {
            response ->
            Log.d("MenuAgregarContacto", "Respuesta del servidor: $response")
                val contactos = mutableListOf<UsuarioResponse>()
                try {
                    val jsonArray = response.getJSONArray("usuarios")
                    for (i in 0 until jsonArray.length()) {
                        val usuario = jsonArray.getJSONObject(i)
                        contactos.add(
                            UsuarioResponse(
                                usuario.getString("nombreUsuario"),
                                usuario.getString("nombreCuenta"),
                                usuario.getString("descripcion")
                            )
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                if (contactos.isEmpty()) {
                    // Si no se encuentran usuarios entonces no se muestra nada
                    recyclerView.adapter = null
                } else {
                    // Si se encuentra usuarios hay que ir actualizando
                    val contactAdapter = ContactAdapter(contactos)
                    recyclerView.adapter = contactAdapter
                }
            },
            Response.ErrorListener { error ->
                // Manejar error
                Log.e("MenuAgregarContacto", "Error en la solicitud: ${error.message}")
                error.printStackTrace()
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }

        queue.add(request)
    }
}