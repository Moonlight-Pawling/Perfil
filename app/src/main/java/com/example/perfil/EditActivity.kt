package com.example.perfil

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.perfil.databinding.ActivityEditBinding
import com.example.perfil.databinding.ActivityMainBinding

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etnombre.setText(intent.extras?.getString(getString(R.string.k_name)))
        binding.etcorreo.setText(intent.extras?.getString(getString(R.string.k_email)))
        binding.etsitioweb.setText(intent.extras?.getString(getString(R.string.k_web)))
        binding.etphone.setText(intent.extras?.getString(getString(R.string.k_phone)))
        binding.etnombre.setText(intent.extras?.getString(getString(R.string.k_lat)))
        binding.etnombre.setText(intent.extras?.getString(R.string.k_lat).toString())
        binding.etnombre.setText(intent.extras?.getString(R.string.k_lon).toString())

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_save){

        }
        return super.onOptionsItemSelected(item)
    }

    fun sendData() {
        val intent = Intent()
        intent.putExtra(getString(R.string.k_name),binding.etnombre.text.toString())
        intent.putExtra(getString(R.string.k_email),binding.etcorreo.toString())
        intent.putExtra(getString(R.string.k_web),binding.etsitioweb.toString())
        intent.putExtra(getString(R.string.k_phone),binding.etphone.toString())
        intent.putExtra(getString(R.string.k_lat),binding.etlat.text.toString())
        intent.putExtra(getString(R.string.k_lon),binding.etlon.text.toString())

        setResult(RESULT_OK,intent)
        finish()
    }
}