package com.example.sora

import android.annotation.SuppressLint
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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.sora.Adapter.NotificacionAdapter
import com.example.sora.Controllers.Constants
import com.example.sora.Controllers.SSLSocketFactoryUtil
import com.example.sora.Datos.SolicitudAmistad
import org.json.JSONArray

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

        val reciclerView : RecyclerView = vista.findViewById(R.id.mostrarNotificacionesSolicitudAmistad)
        reciclerView.layoutManager = LinearLayoutManager(context)

        cargarNotificaciones()

        // Inflate the layout for this fragment
        return vista
    }

    private fun cargarNotificaciones() {
        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(context, sslSocketFactory)

        val request = JsonArrayRequest(Request.Method.GET, Constants.URL_RecibirSolicitudAmistad, null,
            { response ->
                estadoNotificaciones(response)
            },
            { error ->
                Log.e("NotificacionesFragment", "Error: ${error.message}")
            })

        queue.add(request)
    }

    private fun estadoNotificaciones(response: JSONArray) {
        for (i in 0 until response.length()) {
            val jsonObject = response.getJSONObject(i)
            val solicitudAmistad = SolicitudAmistad(
                jsonObject.getString("usuarioEnvia"),
                jsonObject.getString("usuarioRecibe"),
                jsonObject.getString("estado")
            )
            notificaciones.add(solicitudAmistad)
        }
        notificacionAdapter.notifyDataSetChanged()
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