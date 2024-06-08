package com.example.perfil

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        //Programar el botón de regreso.
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = "Configuraciones"
        }

        //Añadimos el fragmento a la actividad
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, SettingsFragment())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}