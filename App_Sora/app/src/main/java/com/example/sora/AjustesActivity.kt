package com.example.sora

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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

        val sharedPreferences = getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)

        btnGuardar.setOnClickListener {
            val nombreCuenta = sharedPreferences.getString("nombreCuenta", null)
            var cambiarNombre = txtCambiarNombre.text.toString()
            var cambiarDescripcion = txtCambiarDescripcion.text.toString()

            if (!cambiarNombre.isNullOrBlank()){
                // Crear una instancia de RequestQueue
                val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
                val queue = Volley.newRequestQueue(this, sslSocketFactory)

                // Crear el objeto JSON con los datos del usuario
                val jsonObject = JSONObject()
                jsonObject.put("NombreCuenta", nombreCuenta)
                jsonObject.put("NombreUsuario", cambiarNombre)
                jsonObject.put("Descripcion", cambiarDescripcion)

                Log.d("AjustesActivity", "Se envia como datos: NombreCuenta: $nombreCuenta , NombreUsuario: $cambiarNombre y descripcion: $cambiarDescripcion")

                // Crear la solicitud de registro
                val ajustesRequest = object : StringRequest(
                    Request.Method.PUT, Constants.URL_ModificarDatosUsuario, Response.Listener {
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
                                putString("nombreUsuario", cambiarNombre)
                                putString("descripcion", cambiarDescripcion)
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

                // Agregar la solicitud a la cola
                queue.add(ajustesRequest)
            } else {
                // Aviso por nombre usuario
                Toast.makeText(this, R.string.errorNombreUsuario, Toast.LENGTH_SHORT).show()
            }
        }

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