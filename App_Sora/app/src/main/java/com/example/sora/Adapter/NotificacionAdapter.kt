package com.example.sora.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sora.R
import com.example.sora.Datos.SolicitudAmistad

class NotificacionAdapter(private val notificaciones: MutableList<SolicitudAmistad>) : RecyclerView.Adapter<NotificacionAdapter.MostrarNotificacion>() {

    class MostrarNotificacion(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textNombreContactoEnvia: TextView = itemView.findViewById(R.id.textNotificationNombreContacto)
        val btnAceptar: Button = itemView.findViewById(R.id.btnNotificacionAceptar)
        val btnRechazar: Button = itemView.findViewById(R.id.btnNotificacionRechazar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostrarNotificacion {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notificaciones_custom, parent, false)
        return MostrarNotificacion(view)
    }

    override fun onBindViewHolder(holder: MostrarNotificacion, position: Int) {
        val notificacion = notificaciones[position]
        holder.textNombreContactoEnvia.text = notificacion.usuarioEnvia

        holder.btnAceptar.setOnClickListener {

        }

        holder.btnRechazar.setOnClickListener {

        }

    }

    override fun getItemCount(): Int {
        return notificaciones.size
    }
}