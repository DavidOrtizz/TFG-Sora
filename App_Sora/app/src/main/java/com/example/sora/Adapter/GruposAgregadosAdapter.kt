package com.example.sora.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sora.Activity.ChatGrupoActivity
import com.example.sora.Datos.GrupoResponse
import com.example.sora.R

class GruposAgregadosAdapter (private val grupos: MutableList<GrupoResponse>) : RecyclerView.Adapter<GruposAgregadosAdapter.MostrarGrupos>() {

    class MostrarGrupos(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textNombreGrupo: TextView = itemView.findViewById(R.id.textNombreGrupo)

        fun bind(grupo: GrupoResponse, clickListener: (GrupoResponse) -> Unit) {
            textNombreGrupo.text = grupo.Nombre

            itemView.setOnClickListener {
                clickListener(grupo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostrarGrupos {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grupo_agregado_custom, parent, false)
        return MostrarGrupos(view)
    }


    override fun onBindViewHolder(holder: MostrarGrupos, position: Int) {
        val grupo = grupos[position]
        holder.bind(grupo) { selectedGrupo ->
            val context = holder.itemView.context

            // Al pulsar se entra al chat
            val intentChat = Intent(context, ChatGrupoActivity::class.java)
                .putExtra("grupoId",grupo.Id.toString())
                .putExtra("nombreGrupo",grupo.Nombre)

            Log.d("GrupoAgregadoAdapter","Has seleccionado el grupo con nombre: ${grupo.Nombre} y con id: ${grupo.Id}")
            context.startActivity(intentChat)
        }
    }

    override fun getItemCount(): Int {
        return grupos.size
    }
}