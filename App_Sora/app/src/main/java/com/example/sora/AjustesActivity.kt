package com.example.sora

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AjustesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ajustes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intentPerfil = Intent(this, MainActivity::class.java)
            .putExtra("cargarMenu","Perfil")
        val intentCerrarSesion = Intent(this, PrimeraVezActivity::class.java)
        val btnGuardar = findViewById<Button>(R.id.buttonGuardar)
        val btnVolver = findViewById<ImageButton>(R.id.buttonVolver)
        val btnCerrarSesion = findViewById<Button>(R.id.buttonCerrarSesion)
        val txtCambiarNombre = findViewById<EditText>(R.id.textCambiarNombreUsuario)
        val txtCambiarDescripcion = findViewById<EditText>(R.id.textCambiarDescripcion)

        val sharedPreferences = getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)

        btnVolver.setOnClickListener {
            startActivity(intentPerfil)
            finish()
        }

        btnCerrarSesion.setOnClickListener {
            // Borrado de datos
            sharedPreferences.edit()
                .clear()
                .apply()

            startActivity(intentCerrarSesion)
            finish()
        }
    }
}