package com.example.sora

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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

        }

        btnCerrar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}