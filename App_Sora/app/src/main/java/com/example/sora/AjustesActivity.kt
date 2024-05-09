package com.example.sora

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
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
        val btnGuardar = findViewById<Button>(R.id.buttonGuardar)
        val btnVolver = findViewById<ImageButton>(R.id.buttonVolver)

        btnVolver.setOnClickListener {
            startActivity(intentPerfil)
            finish()
        }
    }
}