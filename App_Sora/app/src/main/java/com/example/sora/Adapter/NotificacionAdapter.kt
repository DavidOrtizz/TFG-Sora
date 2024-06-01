package com.example.sora.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.sora.Controllers.Constants
import com.example.sora.Controllers.SSLSocketFactoryUtil
import com.example.sora.R
import com.example.sora.Datos.SolicitudAmistad
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificacionAdapter(private val context: Context, private val notificaciones: MutableList<SolicitudAmistad>) : RecyclerView.Adapter<NotificacionAdapter.MostrarNotificacion>() {

    class MostrarNotificacion(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            CoroutineScope(Dispatchers.IO).launch {
                aceptarSolicitud(notificacion, position)
            }
        }

        holder.btnRechazar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                rechazarSolicitud(notificacion, position)
            }
        }
    }

    private fun aceptarSolicitud(notificacion: SolicitudAmistad, position: Int) {
        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(context, sslSocketFactory)
        val url = "${Constants.URL_AceptarSolicitudAmistad}?usuarioEnvia=${notificacion.usuarioEnvia}&usuarioRecibe=${notificacion.usuarioRecibe}"

        val request = JsonObjectRequest(Request.Method.POST, url, null, {
            response ->
            Log.d("NotificacionAdapter", "Solicitud aceptada: $response")
            notificaciones.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, notificaciones.size)
            Toast.makeText(context, R.string.aceptarSolicitudAmistad, Toast.LENGTH_SHORT).show()
        }, { error ->
            Log.e("NotificacionAdapter", "Error: ${error.message}")
            error.printStackTrace()
        })

        queue.add(request)
    }

    private fun rechazarSolicitud(notificacion: SolicitudAmistad, position: Int) {
        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(context, sslSocketFactory)
        val url = "${Constants.URL_RechazarSolicitudAmistad}?usuarioEnvia=${notificacion.usuarioEnvia}&usuarioRecibe=${notificacion.usuarioRecibe}"

        val request = JsonObjectRequest(Request.Method.POST, url, null, {
            response ->
            Log.d("NotificacionAdapter", "Solicitud rechazada: $response")
            notificaciones.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, notificaciones.size)
            Toast.makeText(context, R.string.rechazarSolicitudAmistad, Toast.LENGTH_SHORT).show()
        }, { error ->
            Log.e("NotificacionAdapter", "Error: ${error.message}")
            error.printStackTrace()
        })

        queue.add(request)
    }

    override fun getItemCount(): Int {
        return notificaciones.size
    }
}