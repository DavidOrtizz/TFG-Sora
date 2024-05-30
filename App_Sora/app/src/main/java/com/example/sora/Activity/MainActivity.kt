package com.example.sora.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sora.ContactosFragment
import com.example.sora.GruposFragment
import com.example.sora.NotificacionesFragment
import com.example.sora.PerfilFragment
import com.example.sora.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    // Instancias de los fragmentos
    val gruposFragment = GruposFragment()
    val contactosFragment = ContactosFragment()
    val notificacionesFragment = NotificacionesFragment()
    val perfilFragment = PerfilFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val cargarMenu = intent.getStringExtra("cargarMenu")
        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.gruposFragment -> {
                    loadFragment(gruposFragment)
                    true
                }
                R.id.contactosFragment -> {
                    loadFragment(contactosFragment)
                    true
                }
                R.id.notificacionesFragment -> {
                    loadFragment(notificacionesFragment)
                    true
                }
                R.id.perfilFragment -> {
                    loadFragment(perfilFragment)
                    true
                }
                else -> false
            }
        }

        // Control de los fragments
        if (cargarMenu == null){
            // Selecciona por defecto el fragmento de Contactos
            navigation.selectedItemId = R.id.contactosFragment
        } else if (cargarMenu == "Perfil"){
            navigation.selectedItemId = R.id.perfilFragment
        } else if (cargarMenu == "Contactos"){
            navigation.selectedItemId = R.id.contactosFragment
        } else if (cargarMenu == "Grupos"){
            navigation.selectedItemId = R.id.gruposFragment
        } else if (cargarMenu == "Notificaciones"){
            navigation.selectedItemId = R.id.notificacionesFragment
        } else {
            navigation.selectedItemId = R.id.contactosFragment
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_container, fragment)
            commit()
        }
    }
}
