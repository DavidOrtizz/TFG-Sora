package com.example.sora

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter (private val contacts: List<UsuarioResponse>) : RecyclerView.Adapter<ContactAdapter.MostrarContacto>() {

    class MostrarContacto(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreUsuario: TextView = itemView.findViewById(R.id.textNombreUsuario)
        val nombreCuenta: TextView = itemView.findViewById(R.id.textNombreCuenta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostrarContacto {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contactos_custom, parent, false)
        return MostrarContacto(view)
    }

    override fun onBindViewHolder(holder: MostrarContacto, position: Int) {
        holder.nombreUsuario.text = contacts[position].NombreUsuario
        holder.nombreCuenta.text = contacts[position].NombreCuenta
    }

    override fun getItemCount(): Int = contacts.size
}