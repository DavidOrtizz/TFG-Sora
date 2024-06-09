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
import com.example.sora.Activity.MenuAgregarContacto
import com.example.sora.Adapter.ContactosAdapter
import com.example.sora.Controllers.Constants
import com.example.sora.Controllers.SSLSocketFactoryUtil
import com.example.sora.Datos.ContactosResponse
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
 * Use the [ContactosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactosFragment : Fragment() {
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

    private lateinit var contactosRecyclerView: RecyclerView
    private lateinit var contactosAdapter: ContactosAdapter
    private val contactos = mutableListOf<ContactosResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_contactos, container, false)

        val context = requireContext()

        val btnAgregarContacto = vista.findViewById<FloatingActionButton>(R.id.buttonContactos)
        val intentAgregarContacto = Intent(requireContext(), MenuAgregarContacto::class.java)

        btnAgregarContacto.setOnClickListener {
            activity?.startActivity(intentAgregarContacto)
        }

        val sharedPreferences = context.getSharedPreferences("com.example.sora.DatosUsuario", Context.MODE_PRIVATE)
        val nombreCuenta = sharedPreferences.getString("nombreCuenta", null)

        contactosRecyclerView = vista.findViewById(R.id.contactRv)
        contactosRecyclerView.layoutManager = LinearLayoutManager(context)
        contactosAdapter = ContactosAdapter(contactos)
        contactosRecyclerView.adapter = contactosAdapter

        CoroutineScope(Dispatchers.IO).launch {
            cargarContactos(nombreCuenta.toString())
        }

        // Inflate the layout for this fragment
        return vista
    }

    private fun cargarContactos(nombreCuenta: String) {
        val sslSocketFactory = SSLSocketFactoryUtil.getSSLSocketFactory()
        val queue = Volley.newRequestQueue(context, sslSocketFactory)
        val url = "${Constants.URL_ObtenerContactos}?usuario=${nombreCuenta}"

        val request = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            Log.d("ContactosFragment", "Respuesta del servidor: $response")
            val contactos = mutableListOf<ContactosResponse>()
            try {
                for (i in 0 until response.length()) {
                    val contacto = response.getJSONObject(i)
                    contactos.add(
                        ContactosResponse(
                            contacto.getString("nombreCuenta"),
                            contacto.getString("nombreUsuario")
                        )
                    )
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            if (contactos.isEmpty()) {
                contactosRecyclerView.adapter = null
            } else {
                this.contactos.clear()
                this.contactos.addAll(contactos)
                contactosAdapter.notifyDataSetChanged()
            }
        }, { error ->
            Log.e("ContactosFragment", "Error en la solicitud: ${error.message}")
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
         * @return A new instance of fragment ContactosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}