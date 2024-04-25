package com.example.sora

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
        val btnIniciarSesion = findViewById<Button>(R.id.buttonIniciarSesion)
        val btnRegistrarse = findViewById<TextView>(R.id.textViewRegistrarse)

        btnIniciarSesion.setOnClickListener {
            startActivity(intentIniciarSesion)
            finish()
        }

        btnRegistrarse.setOnClickListener {
            startActivity(intentRegistrarse)
            finish()
        }
    }
}