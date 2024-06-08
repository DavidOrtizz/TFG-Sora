package com.example.sora.Activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.sora.Adapter.GruposAdapter
import com.example.sora.Controllers.Constants
import com.example.sora.Controllers.SSLSocketFactoryUtil
import com.example.sora.Datos.GrupoResponse
import com.example.sora.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class MenuAgregarGrupo : AppCompatActivity() {
    private lateinit var recyclerViewMostrarGrupos: RecyclerView
    private lateinit var gruposAdapter: GruposAdapter
    private val grupos = mutableListOf<GrupoResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_agregar_grupo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnVolver = findViewById<ImageButton>(R.id.buttonVolver)
        val buscarGrupo = findViewById<EditText>(R.id.EditBuscador)
        val btnCrearGrupo = findViewById<FloatingActionButton>(R.id.buttonCrearGrupo)
        recyclerViewMostrarGrupos = findViewById(R.id.gruposRv)

        val sharedPreferences = getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)
        val id = sharedPreferences.getInt("id", 0)

        // Configuración del RecyclerView
        recyclerViewMostrarGrupos.layoutManager = LinearLayoutManager(this)
        gruposAdapter = GruposAdapter(grupos, id)
        recyclerViewMostrarGrupos.adapter = gruposAdapter

        btnVolver.setOnClickListener {
            val intentVolver = Intent(this, MainActivity::class.java)
                .putExtra("cargarMenu", "Grupos")

            startActivity(intentVolver)
            finish()
        }

        btnCrearGrupo.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.crear_grupo)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val textNombreGrupo = dialog.findViewById<EditText>(R.id.textNombreGrupo)
            val btnCrear = dialog.findViewById<Button>(R.id.buttonCrear)
            val btnCerrar = dialog.findViewById<Button>(R.id.buttonCerrar)

            btnCrear.setOnClickListener {
                val nombreGrupo = textNombreGrupo.text.toString()

                if (!nombreGrupo.isNullOrBlank()) {
                    Log.d("MenuCrearGrupo","El nombre del grupo es: $nombreGrupo")

                    CoroutineScope(Dispatchers.IO).launch {
                        crearGrupo(nombreGrupo)
                    }
                    dialog.dismiss()
                } else {
                    Log.d("MenuCrearGrupo","Error el nombre del grupo es: $nombreGrupo")

                    Toast.makeText(this, R.string.errorNombreGrupo, Toast.LENGTH_SHORT).show()
                }
            }

            btnCerrar.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        CoroutineScope(Dispatchers.IO).launch {
            buscarGrupo.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrEmpty()) {
                        Log.d("MenuAgregarGrupo", "Buscando grupo: $s")
                        buscarGrupo(s.toString())
                    }
                }
            })
        }

        // Carga todos los grupos por defecto
        cargarGrupos()
    }

    private fun crearGrupo(nombreGrupo: String) {
        Log.d("CrearGrupo","El nombre del grupo es: $nombreGrupo")

        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(this, sslSocketFactory)

        val jObject = JSONObject().apply {
            put("Nombre", nombreGrupo)
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, Constants.URL_CrearGrupo, jObject,
            { response ->
                try {
                    val grupoId = response.getInt("grupoId")
                    Toast.makeText(this, R.string.exitoCreacionGrupo, Toast.LENGTH_SHORT).show()

                    // Creo el grupo y me uno automaticamente
                    unirseAGrupo(grupoId)

                    cargarGrupos() // Recargo los grupos al crearlo
                } catch (e: JSONException){
                    Log.e("CrearGrupo", "Error: ${e.message}")
                }
            },
            { error ->
                Toast.makeText(this, R.string.errorCreacionGrupo, Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(jsonObjectRequest)
    }

    private fun unirseAGrupo(grupoId: Int) {
        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(this, sslSocketFactory)

        val sharedPreferences = getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)
        val id = sharedPreferences.getInt("id", 0)

        val jObject = JSONObject().apply {
            put("usuarioId", id)
            put("grupoId", grupoId)
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, Constants.URL_UnirseGrupo, jObject, {
            response ->
                Toast.makeText(this, R.string.exitoUnirseGrupo, Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(this, R.string.errorUnirseGrupo, Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(jsonObjectRequest)
    }

    private fun cargarGrupos() {
        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(this, sslSocketFactory)

        val request = JsonArrayRequest(Request.Method.GET, Constants.URL_ObtenerGrupos, null, { response ->
            val nuevosGrupos = mutableListOf<GrupoResponse>()

            for (i in 0 until response.length()) {
                val grupo = response.getJSONObject(i)
                Log.d("cargarGrupos", "Grupo recibido: $grupo")
                try {
                    val id = grupo.getInt("id")
                    val nombre = grupo.getString("nombre")
                    nuevosGrupos.add(
                        GrupoResponse(
                            id,
                            nombre
                        )
                    )
                } catch (e: JSONException) {
                    Log.e("cargarGrupos", "Error en el grupo $i: ${e.message}")
                }
            }
            if (nuevosGrupos.isEmpty()) {
                recyclerViewMostrarGrupos.adapter = null
            } else {
                grupos.clear()
                grupos.addAll(nuevosGrupos)
                gruposAdapter.notifyDataSetChanged()
            }
        }, { error ->
            Toast.makeText(this, R.string.errorCargarGrupo, Toast.LENGTH_SHORT).show()
            Log.e("cargarGrupos", "Error al cargar los grupos: ${error.message}")
        })

        queue.add(request)
    }

    private fun buscarGrupo(nombreGrupo: String) {
        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(this, sslSocketFactory)
        val recyclerView: RecyclerView = findViewById(R.id.gruposRv)

        val jsonObject = JSONObject().apply {
            put("Nombre", nombreGrupo)
        }

        val request = object : JsonObjectRequest(Request.Method.POST, Constants.URL_BuscarGrupos, jsonObject, { response ->
            Log.d("MenuAgregarGrupo", "Respuesta del servidor: $response")
            val nuevosGrupos = mutableListOf<GrupoResponse>()
            try {
                val jsonArray = response.getJSONArray("grupos")
                for (i in 0 until jsonArray.length()) {
                    val grupo = jsonArray.getJSONObject(i)
                    val id = grupo.getInt("id")
                    val nombre = grupo.getString("nombre")
                    nuevosGrupos.add(GrupoResponse(id, nombre))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            if (nuevosGrupos.isEmpty()) {
                recyclerView.adapter = null
            } else {
                grupos.clear()
                grupos.addAll(nuevosGrupos)
                gruposAdapter.notifyDataSetChanged()
            }
        }, { error ->
            // Manejar error
            Log.e("MenuBuscarGrupo", "Error en la solicitud: ${error.message}")
            error.printStackTrace()
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }

        queue.add(request)
    }
}