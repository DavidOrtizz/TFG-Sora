package com.example.sora.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.sora.Datos.MensajeGrupoResponse
import com.example.sora.R

class ChatGrupoAdapter (private val mensajes: List<MensajeGrupoResponse>, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_ENVIAR = 1
    private val TYPE_RECIBIR = 2

    override fun getItemViewType(position: Int): Int {
        val sharedPreferencesDatosUsuario = context.getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)

        val emisor = sharedPreferencesDatosUsuario.getString("nombreCuenta", null)

        // Aquí se controla si el mensaje ha sido enviado o recibido
        return if (mensajes[position].emisor == emisor) {
            TYPE_ENVIAR
        } else {
            TYPE_RECIBIR
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        // Aquí se cambia el diseño que se muestra
        return if (viewType == TYPE_ENVIAR) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_container_enviar_mensaje, parent, false)
            EnviarMensajeViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_container_recibir_mensaje_grupo, parent, false)
            RecibirMensajeViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mensaje = mensajes[position]
        if (holder is EnviarMensajeViewHolder) {
            holder.bind(mensaje)
        } else if (holder is RecibirMensajeViewHolder) {
            holder.bind(mensaje)
        }
    }

    override fun getItemCount(): Int = mensajes.size

    class EnviarMensajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textoMensaje: TextView = itemView.findViewById(R.id.textMensaje)

        fun bind(mensaje: MensajeGrupoResponse) {
            textoMensaje.text = mensaje.contenido
        }
    }

    class RecibirMensajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textoMensaje: TextView = itemView.findViewById(R.id.textMensaje)
        private val nombreUsuario: TextView = itemView.findViewById(R.id.textNombreUsuario)

        fun bind(mensaje: MensajeGrupoResponse) {
            textoMensaje.text = mensaje.contenido
            nombreUsuario.text = mensaje.emisor
        }
    }
}