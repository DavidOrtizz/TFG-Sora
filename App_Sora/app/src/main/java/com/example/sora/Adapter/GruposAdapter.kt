package com.example.sora.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sora.Activity.MainActivity
import com.example.sora.Controllers.Constants
import com.example.sora.Controllers.SSLSocketFactoryUtil
import com.example.sora.Datos.GrupoResponse
import com.example.sora.R
import org.json.JSONObject

class GruposAdapter(private val grupos: List<GrupoResponse>, private val usuarioId: Int) :
    RecyclerView.Adapter<GruposAdapter.MostrarGrupo>() {

    class MostrarGrupo(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreGrupo: TextView = itemView.findViewById(R.id.textNombreGrupo)
        val btnUnirse: Button = itemView.findViewById(R.id.UnirseGrupo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostrarGrupo {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grupo_custom, parent, false)
        return MostrarGrupo(view)
    }

    override fun onBindViewHolder(holder: MostrarGrupo, position: Int) {
        val grupo = grupos[position]
        holder.nombreGrupo.text = grupo.Nombre

        holder.btnUnirse.setOnClickListener {
            unirse(holder.itemView.context, usuarioId ,grupo.Id)
        }
    }

    override fun getItemCount(): Int {
        return grupos.size
    }

    private fun unirse(context: Context, usuarioId: Int, grupoId: Int) {
        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(context, sslSocketFactory)

        val url = "${Constants.URL_UnirseGrupo}?usuarioId=$usuarioId&grupoId=$grupoId"

        Log.d("GruposAdapter", "Datos que se envian: UsuarioId: $usuarioId , GrupoId: $grupoId")

        val request = StringRequest(Request.Method.POST, url, {
            response ->
            Toast.makeText(context, R.string.exitoUnirseGrupo, Toast.LENGTH_SHORT).show()
        }, { error ->
            Log.e("GruposAdapter", "Error: ${error.message}")
            Toast.makeText(context, R.string.errorUnirseGrupo, Toast.LENGTH_SHORT).show()
        })

        queue.add(request)
    }
}