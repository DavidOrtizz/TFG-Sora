package com.example.sora.Activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sora.Controllers.Constants
import com.example.sora.R
import com.example.sora.Controllers.SSLSocketFactoryUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject

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
        val iconoPerfil = findViewById<ImageView>(R.id.imageViewPerfil)

        val sharedPreferences = getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)

        // Función para mostrar u ocultar el botón guardar
        fun mostrarBotonGuardar() {
            val nombreNoVacio = !txtCambiarNombre.text.isNullOrBlank()
            val descripcionNoVacia = !txtCambiarDescripcion.text.isNullOrBlank()

            if (nombreNoVacio || descripcionNoVacia) {
                btnGuardar.show()
            } else {
                btnGuardar.hide()
            }
        }

        txtCambiarNombre.addTextChangedListener {
            mostrarBotonGuardar()
        }

        txtCambiarDescripcion.addTextChangedListener {
            mostrarBotonGuardar()
        }

        btnGuardar.setOnClickListener {
            val nombreCuenta = sharedPreferences.getString("nombreCuenta", null)
            var cambiarNombre = txtCambiarNombre.text.toString()
            var cambiarDescripcion = txtCambiarDescripcion.text.toString()

            // Crear una instancia de RequestQueue
            val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
            val queue = Volley.newRequestQueue(this, sslSocketFactory)

            // Crear el objeto JSON con los datos del usuario
            val jsonObject = JSONObject()
            jsonObject.put("NombreCuenta", nombreCuenta)
            if (!cambiarNombre.isNullOrBlank()) { // Controlo que el nombre no sea vacio
                jsonObject.put("NombreUsuario", cambiarNombre)
            }
            if (!cambiarDescripcion.isNullOrBlank()){
                jsonObject.put("Descripcion", cambiarDescripcion) // Controlo la descripcion
            }
            Log.d("AjustesActivity", "Se envia como datos: NombreCuenta: $nombreCuenta , NombreUsuario: $cambiarNombre y descripcion: $cambiarDescripcion")

            // Crear la solicitud de registro
            val ajustesRequest = object : StringRequest(Request.Method.PUT, Constants.URL_ModificarDatosUsuario, Response.Listener {
                response ->
                    try {
                        Log.d("AjustesActivity", "Respuesta del servidor: $response")

                        // Procesar la respuesta del servidor
                        val jsonResponse = JSONObject(response)
                        val mensaje = jsonResponse.getString("mensaje")

                        Log.d("AjustesActivity", "Mensaje recibido: $mensaje")

                        if (mensaje == "Datos del usuario actualizados") {
                            // Exito en la modificación
                            Toast.makeText(this, R.string.exitoModificacion, Toast.LENGTH_SHORT).show()

                            sharedPreferences.edit().apply {
                                if (!cambiarNombre.isNullOrBlank()) { // Controlo que el nombre no sea vacio
                                    putString("nombreUsuario", cambiarNombre)
                                }
                                if (!cambiarDescripcion.isNullOrBlank()){
                                    putString("descripcion", cambiarDescripcion)
                                }
                                apply()
                            }
                        } else {
                            // Fallo en la modificación
                            Toast.makeText(this, R.string.fallorModificacion, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    // Manejar errores de la solicitud
                    Toast.makeText(this, R.string.errorModificarDaros, Toast.LENGTH_SHORT).show()
                    Log.d("AjustesActivity", error.toString())
                }) {
                override fun getBody(): ByteArray {
                    return jsonObject.toString().toByteArray()
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }
            }

            txtCambiarNombre.text.clear()
            txtCambiarDescripcion.text.clear()
            queue.add(ajustesRequest)
        }

        btnVolver.setOnClickListener {
            startActivity(intentPerfil)
            finish()
        }

        btnCerrarSesion.setOnClickListener {
            showCustomDialogBox()
        }

        iconoPerfil.setOnClickListener {
            cambiarIconoPerfil()
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

    private fun cambiarIconoPerfil(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.cambiar_perfil)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val iconoCalcifer : ImageView = dialog.findViewById(R.id.iconoCalcifer)
        val iconoGato : ImageView = dialog.findViewById(R.id.iconoGato)
        val iconoDesdentao : ImageView = dialog.findViewById(R.id.iconoDesdentao)
        val btnCancelar : Button = dialog.findViewById(R.id.buttonCancelar)
        val sharedPreferences = getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)

        iconoCalcifer.setOnClickListener {
            actualizarIconoPerfil("icono_calcifer1.png")
        }

        iconoGato.setOnClickListener {
            actualizarIconoPerfil("icono_gato3.png")
        }

        iconoDesdentao.setOnClickListener {
            actualizarIconoPerfil("icono_desdentao2.png")
        }

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // No funciona :/
    private fun actualizarIconoPerfil(icono: String) {
        val sharedPreferences = getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)
        val nombreCuenta = sharedPreferences.getString("nombreCuenta", null)

        if (nombreCuenta != null) {
            val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
            val queue = Volley.newRequestQueue(this, sslSocketFactory)

            val jsonObject = JSONObject()
            jsonObject.put("NombreCuenta", nombreCuenta)
            jsonObject.put("IconoPerfil", icono)

            val iconRequest = object : StringRequest(Request.Method.PUT,
                Constants.URL_ModificarIconoPerfil, Response.Listener {
                response ->
                    try {
                        val jsonResponse = JSONObject(response)
                        val mensaje = jsonResponse.getString("mensaje")

                        if (mensaje == "Icono actualizado") {
                            Toast.makeText(this, R.string.exitoModificacion, Toast.LENGTH_SHORT)
                                .show()
                            sharedPreferences.edit().putString("iconoPerfil", icono).apply()
                        } else {
                            Toast.makeText(this, R.string.fallorModificacion, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    Toast.makeText(this, R.string.errorModificarDaros, Toast.LENGTH_SHORT).show()
                }) {
                override fun getBody(): ByteArray {
                    return jsonObject.toString().toByteArray()
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }
            }

            queue.add(iconRequest)
        } else {
            Toast.makeText(this, R.string.errorNombreUsuario, Toast.LENGTH_SHORT).show()
        }
    }
}