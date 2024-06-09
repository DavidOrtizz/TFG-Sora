package com.example.sora.Activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sora.Adapter.ChatAdapter
import com.example.sora.Controllers.Constants
import com.example.sora.Controllers.SSLSocketFactoryUtil
import com.example.sora.Datos.MensajeResponse
import com.example.sora.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private val mensajes = mutableListOf<MensajeResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnVolver = findViewById<AppCompatImageView>(R.id.buttonVolver)
        val btnInfo = findViewById<AppCompatImageView>(R.id.buttonInfo)
        val nombreUsuario = findViewById<TextView>(R.id.TextNombreUsuario)
        val escribirMensaje = findViewById<EditText>(R.id.textEscribirMensaje)
        val btnEnviar = findViewById<FrameLayout>(R.id.buttonEnviar)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        recyclerView = findViewById(R.id.chatRecyclerView)

        // Configuracion del RecyclerView
        adapter = ChatAdapter(mensajes, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Recupero aquí todos los datos necesarios
        val sharedPreferencesDatosUsuario = getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)

        val emisor = sharedPreferencesDatosUsuario.getString("nombreCuenta", null)
        val receptorNombreCuenta = sharedPreferencesDatosUsuario.getString("nombreCuentaReceptor", null)
        val receptorNombreUsuario = sharedPreferencesDatosUsuario.getString("nombreUsuarioReceptor", null)

        Log.d("ChatActivity", "Control de datos: emisor: $emisor , receptorNombreCuenta: $receptorNombreCuenta , receptorNombreUsuario: $receptorNombreUsuario")
        nombreUsuario.text = receptorNombreUsuario

        // Llamo a Firebase para que guarde los mensajes
        database = FirebaseDatabase.getInstance().getReference("mensajes")

        val intentVolver = Intent(this, MainActivity::class.java)
            .putExtra("cargarMenu", "Contactos")

        // Volver al menú de contactos
        btnVolver.setOnClickListener {
            startActivity(intentVolver)
            finish()
        }

        // Botón para entrar en la informacion del receptor y donde se puede eliminar de amigos
        btnInfo.setOnClickListener {
            mostrarMenuInfo(this)
        }

        // Cuando se pulse enviar el mensaje se mostrará en la pantalla y se enviará para que lo reciba el receptor
        btnEnviar.setOnClickListener {
            val contenidoMensaje = escribirMensaje.text.toString()
            if (contenidoMensaje.isNotEmpty()) {
                if (emisor != null && receptorNombreCuenta != null) {
                    enviarMensaje(emisor, receptorNombreCuenta, contenidoMensaje)
                    escribirMensaje.text.clear()
                } else {
                    Toast.makeText(this, R.string.errorEnviarMensaje, Toast.LENGTH_SHORT).show()
                }
            }
        }

        cargarMensajes(emisor, receptorNombreCuenta)
    }

    // Carga todos los mensajes que han enviado los usuarios
    private fun cargarMensajes(emisor: String?, receptor: String?) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        // Cargará los mensajes cuando el emisor y el receptor no sean nulos
        if (emisor != null && receptor != null) {
            val chatId = getChatId(emisor, receptor)

            // Indico que contactos va a cargar los mensajes
            database.child(chatId).addValueEventListener(object : ValueEventListener {

                // Si hay algun cambio en el firebase se traerá los cambios
                override fun onDataChange(snapshot: DataSnapshot) {
                    mensajes.clear()
                    for (mensajeSnapshot in snapshot.children) {
                        val mensaje = mensajeSnapshot.getValue(MensajeResponse::class.java)
                        if (mensaje != null) {
                            mensajes.add(mensaje)
                        }
                    }
                    // Actualiza la interfaz para mostrar los mensajes nuevos
                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                        progressBar.visibility = View.GONE
                    }
                }

                // Registra el error por si se cancela la consulta en el firebase
                override fun onCancelled(error: DatabaseError) {
                    Log.e("ChatActivity", "Error al cargar mensajes: ${error.message}")
                }
            })
        }
    }

    // El mensaje que se ha escrito se envia al firebase y se mostrará en el chat de la conversación
    private fun enviarMensaje(emisor: String, receptor: String, contenidoMensaje: String) {
        val chatId = getChatId(emisor, receptor)
        val mensajeId = database.child(chatId).push().key
        if (mensajeId != null) {
            val mensaje = MensajeResponse(emisor, receptor, contenidoMensaje)
            database.child(chatId).child(mensajeId).setValue(mensaje)
        }
    }

    // Creo identificadores para el firebase y poder guardar las conversaciones entre dos usuarios
    private fun getChatId(emisor: String, receptor: String): String {
        return if (emisor.isNotBlank() && receptor.isNotBlank()) {
            if (emisor < receptor) {
                "${emisor}_$receptor"
            } else {
                "${receptor}_$emisor"
            }
        } else {
            throw IllegalArgumentException("El emisor y el receptor no pueden estar vacios")
        }
    }

    // Botón donde se mostrará un pequeño menú
    private fun mostrarMenuInfo(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.menu_informacion_contacto)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnEliminar = dialog.findViewById<Button>(R.id.buttonEliminarAmigo)
        val btnCerrar = dialog.findViewById<Button>(R.id.buttonCerrar)

        // Eliminia el contacto
        btnEliminar.setOnClickListener {
            val sharedPreferencesDatosUsuario = getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)

            val emisor = sharedPreferencesDatosUsuario.getString("nombreCuenta", null)
            val receptorNombreCuenta = sharedPreferencesDatosUsuario.getString("nombreCuentaReceptor", null)

            if (emisor != null && receptorNombreCuenta != null) {
                eliminarContacto(emisor, receptorNombreCuenta)
            } else {
                Toast.makeText(this, R.string.errorEliminarContacto, Toast.LENGTH_SHORT).show()
            }
        }

        btnCerrar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // Elimina el contacto y vuelve a mandar al menú de Contactos
    private fun eliminarContacto(usuario: String, contacto: String) {
        val url = "${Constants.URL_EliminarContactos}?usuario=$usuario&contacto=$contacto"
        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(this, sslSocketFactory)

        val eliminarContacto = object : StringRequest(Request.Method.DELETE, url,
            Response.Listener { response ->
                Toast.makeText(this, R.string.confirmacionContactoEliminado, Toast.LENGTH_SHORT).show()

                // Volver al menú contactos
                val intentVolver = Intent(this, MainActivity::class.java)
                    .putExtra("cargarMenu", "Contactos")
                startActivity(intentVolver)
                finish()
            },
            Response.ErrorListener { error ->
                Log.e("ChatActivity", "Error al eliminar contacto: ${error.message}")
                Toast.makeText(this, R.string.errorEliminarContacto, Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                return headers
            }
        }

        queue.add(eliminarContacto)
    }
}