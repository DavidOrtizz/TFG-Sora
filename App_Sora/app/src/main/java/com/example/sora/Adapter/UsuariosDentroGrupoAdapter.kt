package com.example.sora.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sora.Controllers.Constants
import com.example.sora.Controllers.SSLSocketFactoryUtil
import com.example.sora.Datos.MiembroGrupoResponse
import com.example.sora.R
import org.json.JSONObject

class UsuariosDentroGrupoAdapter(private val usuarios: MutableList<MiembroGrupoResponse>, private val grupoId: String?, private val context: Context) : RecyclerView.Adapter<UsuariosDentroGrupoAdapter.UsuarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.miembro_grupo_custom, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.bind(usuario, position)
    }

    override fun getItemCount() = usuarios.size

    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.textNombreUsuario)
        private val cuentaTextView: TextView = itemView.findViewById(R.id.textNombreCuenta)
        private val eliminarButton: Button = itemView.findViewById(R.id.buttonEliminar)

        fun bind(usuario: MiembroGrupoResponse, position: Int) {
            nombreTextView.text = usuario.nombreUsuario
            cuentaTextView.text = usuario.nombreCuenta

            eliminarButton.setOnClickListener {
                eliminarUsuarioGrupo(usuario.nombreCuenta, position)
            }
        }
    }

    private fun eliminarUsuarioGrupo(nombreCuenta: String, position: Int) {
        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(context, sslSocketFactory)

        val jsonBody = JSONObject().apply {
            put("grupoId", grupoId)
            put("nombreCuenta", nombreCuenta)
        }

        val request = object : StringRequest(Request.Method.POST, Constants.URL_EliminarUsuarioGrupo, Response.Listener {
                    notifyItemRemoved(position)
            },
            Response.ErrorListener { error ->
                Log.e("EliminarUsuario", "Error al eliminar el usuario: ${error.message}")
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return jsonBody.toString().toByteArray(Charsets.UTF_8)
            }
        }

        queue.add(request)
    }
}