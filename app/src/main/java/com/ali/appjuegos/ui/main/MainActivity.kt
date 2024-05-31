package com.ali.appjuegos.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ali.appjuegos.InicioFragment
import com.ali.appjuegos.NoSessionFragment
import com.ali.appjuegos.ProfileFragment
import com.ali.appjuegos.R
import com.ali.appjuegos.data.LocalDatabase
import com.ali.appjuegos.databinding.ActivityMainBinding
import com.ali.appjuegos.ui.rankings.RankingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inicioFragment = InicioFragment()
        val profileFragment = ProfileFragment()
        val rankingsFragment = RankingsFragment()
        val noSessionFragment = NoSessionFragment()

        setCurrentFragment(inicioFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_inicio -> setCurrentFragment(inicioFragment)
                R.id.nav_perfil -> {
                    // Comprobar si hay un usuario cargado en LocalDatabase.userData
                    if (LocalDatabase.userData != null) {
                        // Si hay un usuario cargado, mostrar el fragmento de perfil
                        setCurrentFragment(profileFragment)
                    } else {
                        // Si no hay un usuario cargado, mostrar el fragmento de no sesión
                        setCurrentFragment(noSessionFragment)
                    }
                }
                R.id.nav_rankings -> setCurrentFragment(rankingsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}
