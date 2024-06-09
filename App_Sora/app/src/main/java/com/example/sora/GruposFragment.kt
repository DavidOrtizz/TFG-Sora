package com.example.sora

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.sora.Activity.MenuAgregarGrupo
import com.example.sora.Adapter.GruposAgregadosAdapter
import com.example.sora.Controllers.Constants
import com.example.sora.Controllers.SSLSocketFactoryUtil
import com.example.sora.Datos.GrupoResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GruposFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GruposFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var gruposRecyclerView: RecyclerView
    private lateinit var gruposAdapter: GruposAgregadosAdapter
    private val grupos = mutableListOf<GrupoResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_grupos, container, false)

        val context = requireContext()

        val btnAgregarGrupo = view.findViewById<FloatingActionButton>(R.id.buttonGrupos)
        val intentAgregarGrupos = Intent(context, MenuAgregarGrupo::class.java)

        btnAgregarGrupo.setOnClickListener {
            startActivity(intentAgregarGrupos)
        }

        val sharedPreferences = context.getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)
        val id = sharedPreferences.getInt("id", 0)

        gruposRecyclerView = view.findViewById(R.id.gruposRv)
        gruposRecyclerView.layoutManager = LinearLayoutManager(context)
        gruposAdapter = GruposAgregadosAdapter(grupos)
        gruposRecyclerView.adapter = gruposAdapter

        CoroutineScope(Dispatchers.IO).launch {
            cargarGrupos()
        }

        return view
    }

    private fun cargarGrupos() {
        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(context, sslSocketFactory)

        val sharedPreferences = requireContext().getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)
        val id = sharedPreferences.getInt("id", 0)

        val request = JsonArrayRequest(Request.Method.GET, Constants.URL_MostrarGruposUnidos+id, null, { response ->
            Log.d("GruposFragment", "Respuesta del servidor: $response")
            val grupos = mutableListOf<GrupoResponse>()
            try {
                for (i in 0 until response.length()) {
                    val grupo = response.getJSONObject(i)
                    val id = grupo.getInt("id")
                    val nombreGrupo = grupo.getString("nombre")

                    grupos.add(
                        GrupoResponse(
                            id,
                            nombreGrupo
                        )
                    )
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            if (grupos.isEmpty()) {
                gruposRecyclerView.adapter = null
            } else {
                this.grupos.clear()
                this.grupos.addAll(grupos)
                gruposAdapter.notifyDataSetChanged()
            }
        }, { error ->
            Log.e("GruposFragment", "Error en la solicitud: ${error.message}")
            error.printStackTrace()
        })

        queue.add(request)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GruposFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GruposFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}