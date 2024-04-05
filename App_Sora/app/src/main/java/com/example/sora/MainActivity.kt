package com.example.sora

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
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

        // Fragmen que se ejecuta por defecto
        loadFragment(contactosFragment)
        // Selecciona el elemento del menú correspondiente al fragmento de Contactos
        navigation.selectedItemId = R.id.contactosFragment
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_container, fragment)
            commit()
        }
    }
}