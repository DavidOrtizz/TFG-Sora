package com.example.sora.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sora.Datos.GrupoResponse
import com.example.sora.R

class GruposAdapter (private val grupos: List<GrupoResponse>) : RecyclerView.Adapter<GruposAdapter.MostrarGrupo>() {

    class MostrarGrupo(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreGrupo: TextView = itemView.findViewById(R.id.textNombreGrupo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostrarGrupo {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grupo_custom, parent, false)
        return MostrarGrupo(view)
    }

    override fun onBindViewHolder(holder: MostrarGrupo, position: Int) {
        val grupo = grupos[position]
        holder.nombreGrupo.text = grupo.Nombre
    }

    override fun getItemCount(): Int {
        return grupos.size
    }
}