package com.example.sora.Adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.sora.Controllers.Constants
import com.example.sora.R
import com.example.sora.Controllers.SSLSocketFactoryUtil
import com.example.sora.Datos.UsuarioResponse
import org.json.JSONObject

class ContactAdapter (private val contactos: List<UsuarioResponse>) : RecyclerView.Adapter<ContactAdapter.MostrarContacto>() {

    class MostrarContacto(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreUsuario: TextView = itemView.findViewById(R.id.textNombreUsuario)
        val nombreCuenta: TextView = itemView.findViewById(R.id.textNombreCuenta)
        val btnVerPerfil: Button = itemView.findViewById(R.id.ButtonVerPerfil)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostrarContacto {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contactos_buscador_custom, parent, false)
        return MostrarContacto(view)
    }

    override fun onBindViewHolder(holder: MostrarContacto, position: Int) {
        val contacto = contactos[position]
        holder.nombreUsuario.text = contactos[position].NombreUsuario
        holder.nombreCuenta.text = contactos[position].NombreCuenta

        holder.btnVerPerfil.setOnClickListener {
            mostrarPerfilContacto(holder.itemView.context, contacto)
        }
    }

    override fun getItemCount(): Int = contactos.size

    private fun mostrarPerfilContacto(context: Context, contacto: UsuarioResponse) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.ver_perfil_contacto)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val nombreUsuario: TextView = dialog.findViewById(R.id.textView_NombreUsuario)
        val nombreCuenta: TextView = dialog.findViewById(R.id.textView_NombreCuenta)
        val descripcion: TextView = dialog.findViewById(R.id.textViewContenidoSobreMi)
        val btnAgregarContacto : Button = dialog.findViewById(R.id.buttonSolicitudAmistad)
        val btnCerrar : Button = dialog.findViewById(R.id.buttonCerrar)

        nombreUsuario.text = contacto.NombreUsuario
        nombreCuenta.text = contacto.NombreCuenta
        descripcion.text = contacto.Descripcion

        btnAgregarContacto.setOnClickListener {
            enviarSolicitudAmistad(context, contacto)
            dialog.dismiss()
        }

        btnCerrar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun enviarSolicitudAmistad(context: Context, contacto: UsuarioResponse) {

        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(context, sslSocketFactory)

        val sharedPreferences = context.getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)
        val nombreCuentaEnvia = sharedPreferences.getString("nombreCuenta", null)

        Log.d("enviarSolicitudAmistad", "Nombre cuenta envia $nombreCuentaEnvia , y usuarioRecibe: ${contacto.NombreCuenta}")
        val jsonObject = JSONObject().apply {
            put("UsuarioEnvia", nombreCuentaEnvia)
            put("UsuarioRecibe", contacto.NombreCuenta)
        }

        val request = object : JsonObjectRequest(Method.POST, Constants.URL_EnviarSolicitudAmistad, jsonObject, Response.Listener {
            response ->
            Toast.makeText(context, R.string.avisoEnviarSolicitudAmistad, Toast.LENGTH_SHORT).show()
            Log.d("ContactAdapter", "Exito al enviar: $response")
        },
            { error ->
                Toast.makeText(context, R.string.avisoErrorSolicitudAmistad, Toast.LENGTH_SHORT).show()
                Log.e("ContactAdapter", "Error: ${error.message}")
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }

        queue.add(request)
    }
}