package com.example.sora.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sora.Datos.ContactosResponse
import com.example.sora.R

class ContactosAdapter (private val contactos: MutableList<ContactosResponse>) : RecyclerView.Adapter<ContactosAdapter.MostrarContactos>()  {
    class MostrarContactos(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textNombreUsuario: TextView = itemView.findViewById(R.id.textNombreUsuario)
        val textNombreCuenta: TextView = itemView.findViewById(R.id.textNombreCuenta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostrarContactos {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contactos_custom, parent, false)
        return MostrarContactos(view)
    }

    override fun onBindViewHolder(holder: MostrarContactos, position: Int) {
        val contacto = contactos[position]
        holder.textNombreUsuario.text = contacto.nombreUsuario
        holder.textNombreCuenta.text = contacto.nombreCuenta
    }

    override fun getItemCount(): Int {
        return contactos.size
    }
}