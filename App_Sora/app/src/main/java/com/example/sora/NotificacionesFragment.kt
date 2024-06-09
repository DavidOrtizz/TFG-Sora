package com.example.sora

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.sora.Activity.MainActivity
import com.example.sora.Adapter.NotificacionAdapter
import com.example.sora.Controllers.Constants
import com.example.sora.Controllers.SSLSocketFactoryUtil
import com.example.sora.Datos.SolicitudAmistad
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
 * Use the [NotificacionesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificacionesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var notificacionAdapter: NotificacionAdapter
    private val notificaciones = mutableListOf<SolicitudAmistad>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_notificaciones, container, false)

        val context = requireContext()

        val intentNotificacion = Intent(context, MainActivity::class.java)
            .putExtra("cargarMenu","Notificaciones")
        val btnActualizar : FloatingActionButton = vista.findViewById(R.id.buttonNotificacionesActualizar)

        recyclerView = vista.findViewById(R.id.mostrarNotificacionesSolicitudAmistad)
        recyclerView.layoutManager = LinearLayoutManager(context)
        notificacionAdapter = NotificacionAdapter(context, notificaciones)
        recyclerView.adapter = notificacionAdapter

        val sharedPreferences = context.getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)
        val nombreCuenta = sharedPreferences.getString("nombreCuenta", null)

        CoroutineScope(Dispatchers.IO).launch {
            cargarNotificaciones(nombreCuenta.toString())
        }

        btnActualizar.setOnClickListener {
            Toast.makeText(context, R.string.actualizarNotificaciones, Toast.LENGTH_SHORT).show()
            startActivity(intentNotificacion)
        }

        // Inflate the layout for this fragment
        return vista
    }

    private fun cargarNotificaciones(nombreCuenta: String) {
        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(requireContext(), sslSocketFactory)

        val url = "${Constants.URL_RecibirSolicitudAmistad}?usuarioRecibe=$nombreCuenta"

        val request = JsonArrayRequest(Request.Method.GET, url, null, Response.Listener{
            response ->
                Log.d("NotificacionesFragment", "Respuesta del servidor: $response")
                val notificaciones = mutableListOf<SolicitudAmistad>()
                try {
                    for (i in 0 until response.length()) {
                        val solicitud = response.getJSONObject(i)
                        notificaciones.add(
                            SolicitudAmistad(
                                solicitud.getString("usuarioEnvia"),
                                solicitud.getString("usuarioRecibe"),
                                solicitud.getString("estado")
                            )
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                    if (notificaciones.isEmpty()) {
                        // Si no se encuentran notificaciones entonces no se muestra nada
                        recyclerView.adapter = null
                    } else {
                        this.notificaciones.clear()
                        this.notificaciones.addAll(notificaciones)
                        notificacionAdapter.notifyDataSetChanged()
                    }
            }
        ) { error ->
            // Manejar error
            Log.e("NotificacionesFragment", "Error en la solicitud: ${error.message}")
            error.printStackTrace()
        }

        queue.add(request)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotificacionesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificacionesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}