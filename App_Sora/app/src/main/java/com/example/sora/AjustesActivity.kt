package com.example.sora

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
        val btnGuardar = findViewById<FloatingActionButton>(R.id.buttonGuardar)
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
            showCustomDialogBox()
        }
    }

    private fun showCustomDialogBox(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val intentCerrarSesion = Intent(this, PrimeraVezActivity::class.java)
        val btnSi : Button = dialog.findViewById(R.id.btnSi)
        val btnNo : Button = dialog.findViewById(R.id.btnNo)
        val sharedPreferences = getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)

        btnSi.setOnClickListener {
            // Borrado de datos
            sharedPreferences.edit()
                .clear()
                .apply()

            startActivity(intentCerrarSesion)
            finish()
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}