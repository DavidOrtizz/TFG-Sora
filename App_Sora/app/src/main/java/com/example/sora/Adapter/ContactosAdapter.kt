package com.example.sora.Adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sora.Activity.ChatActivity
import com.example.sora.Datos.ContactosResponse
import com.example.sora.R

class ContactosAdapter (private val contactos: MutableList<ContactosResponse>) : RecyclerView.Adapter<ContactosAdapter.MostrarContactos>()  {
    class MostrarContactos(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textNombreUsuario: TextView = itemView.findViewById(R.id.textNombreUsuario)
        val textNombreCuenta: TextView = itemView.findViewById(R.id.textNombreCuenta)

        fun guardarDatos(contacto: ContactosResponse, clickListener: (ContactosResponse) -> Unit) {
            textNombreUsuario.text = contacto.nombreUsuario
            textNombreCuenta.text = contacto.nombreCuenta

            itemView.setOnClickListener {
                clickListener(contacto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostrarContactos {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contactos_custom, parent, false)
        return MostrarContactos(view)
    }

    override fun onBindViewHolder(holder: MostrarContactos, position: Int) {
        val contacto = contactos[position]
        holder.guardarDatos(contacto) { selectedContact ->
            val context = holder.itemView.context
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("nombreCuentaReceptor", selectedContact.nombreCuenta)
            editor.putString("nombreUsuarioReceptor", selectedContact.nombreUsuario)
            editor.apply()

            // Al pulsar se entra al chat
            val intentChat = Intent(context, ChatActivity::class.java)
            context.startActivity(intentChat)
        }
    }

    override fun getItemCount(): Int {
        return contactos.size
    }
}