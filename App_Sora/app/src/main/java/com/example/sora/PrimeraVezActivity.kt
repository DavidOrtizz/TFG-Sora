package com.example.sora

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import kotlin.math.log
import org.json.JSONObject
import java.io.IOException

class PrimeraVezActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_primera_vez)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnIniciarSesion = findViewById<Button>(R.id.buttonIniciarSesion)
        val intent = Intent(this, IniciarSesionActivity::class.java)
        val intentAutomatico = Intent(this, MainActivity::class.java)

        btnIniciarSesion.setOnClickListener {
            startActivity(intent)
            finish()
        }
    }
}