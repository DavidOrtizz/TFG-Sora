package com.example.sora

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        val btnRegistrarse = findViewById<Button>(R.id.buttonRegistrarse)
        val btnIniciar = findViewById<TextView>(R.id.textViewIniciar)

        btnRegistrarse.setOnClickListener {
            startActivity(intent)
            finish()
        }

        btnIniciar.setOnClickListener {
            startActivity(intent)
            finish()
        }
    }
}