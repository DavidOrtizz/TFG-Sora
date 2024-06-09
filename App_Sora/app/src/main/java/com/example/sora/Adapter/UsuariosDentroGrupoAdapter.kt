package com.example.sora.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sora.Datos.MiembroGrupoResponse
import com.example.sora.R

class UsuariosDentroGrupoAdapter (private val usuarios: List<MiembroGrupoResponse>) : RecyclerView.Adapter<UsuariosDentroGrupoAdapter.UsuarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.miembro_grupo_custom, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.bind(usuario)
    }

    override fun getItemCount() = usuarios.size

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.textNombreUsuario)
        private val cuentaTextView: TextView = itemView.findViewById(R.id.textNombreCuenta)
        private val eliminarButton: Button = itemView.findViewById(R.id.buttonEliminar)

        fun bind(usuario: MiembroGrupoResponse) {
            nombreTextView.text = usuario.nombreUsuario
            cuentaTextView.text = usuario.nombreCuenta
            eliminarButton.setOnClickListener {

            }
        }
    }
}