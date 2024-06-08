package com.example.sora.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.example.sora.Adapter.ChatGrupoAdapter
import com.example.sora.Datos.MensajeGrupoResponse
import com.example.sora.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatGrupoActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatGrupoAdapter
    private val mensajes = mutableListOf<MensajeGrupoResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_grupo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnVolver = findViewById<AppCompatImageView>(R.id.buttonVolver)
        val btnInfo = findViewById<AppCompatImageView>(R.id.buttonInfo)
        val textNombreGrupo = findViewById<TextView>(R.id.textNombreGrupo)
        val escribirMensaje = findViewById<EditText>(R.id.textEscribirMensaje)
        val btnEnviar = findViewById<FrameLayout>(R.id.buttonEnviar)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        recyclerView = findViewById(R.id.chatRecyclerView)

        // Configuracion del RecyclerView
        adapter = ChatGrupoAdapter(mensajes, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Recupero aquí todos los datos necesarios
        val sharedPreferencesDatosUsuario = getSharedPreferences("com.example.sora.DatosUsuario", MODE_PRIVATE)

        val emisor = sharedPreferencesDatosUsuario.getString("nombreCuenta", null)
        val grupoId = intent.getStringExtra("grupoId")
        val nombreGrupo = intent.getStringExtra("nombreGrupo")

        Log.d("ChatGrupoActivity", "Datos traidos: emisor: $emisor, grupoId:$grupoId, nombreGrupo,$nombreGrupo")
        textNombreGrupo.text = nombreGrupo

        // Llamo a Firebase para que guarde los mensajes
        database = FirebaseDatabase.getInstance().getReference("grupos/$grupoId/mensajes")

        val intentVolver = Intent(this, MainActivity::class.java)
            .putExtra("cargarMenu", "Contactos")

        // Volver al menú de contactos
        btnVolver.setOnClickListener {
            startActivity(intentVolver)
            finish()
        }

        // Botón para entrar en la informacion del receptor y donde se puede eliminar de amigos
        btnInfo.setOnClickListener {
            // Aquí puedes añadir la lógica para el botón de información
        }

        // Cuando se pulse enviar el mensaje se mostrará en la pantalla y se enviará para que lo reciba el receptor
        btnEnviar.setOnClickListener {
            val contenidoMensaje = escribirMensaje.text.toString()
            if (contenidoMensaje.isNotEmpty()) {
                if (emisor != null) {
                    enviarMensaje(emisor, contenidoMensaje)
                    escribirMensaje.text.clear()
                } else {
                    Toast.makeText(this, R.string.errorEnviarMensaje, Toast.LENGTH_SHORT).show()
                }
            }
        }

        cargarMensajes()
    }

    // Carga todos los mensajes que han enviado los usuarios
    private fun cargarMensajes() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mensajes.clear()
                for (mensajeSnapshot in snapshot.children) {
                    val mensaje = mensajeSnapshot.getValue(MensajeGrupoResponse::class.java)
                    if (mensaje != null) {
                        mensajes.add(mensaje)
                    }
                }
                adapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatGrupoActivity", "Error al cargar el mensaje: ${error.message}")
                progressBar.visibility = View.GONE
            }
        })
    }

    // El mensaje que se ha escrito se envia a firebase y se mostrará en el chat
    private fun enviarMensaje(emisor: String, contenidoMensaje: String) {
        val mensajeId = database.push().key
        if (mensajeId != null) {
            val mensaje = MensajeGrupoResponse(emisor, contenidoMensaje)
            database.child(mensajeId).setValue(mensaje).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("ChatGrupoActivity", "Mensaje enviado correctamente")
                } else {
                    Log.e("ChatGrupoActivity", "Error al enviar el mensaje: ${task.exception?.message}")
                }
            }
        }
    }
}