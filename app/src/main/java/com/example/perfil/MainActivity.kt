package com.example.perfil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import com.example.perfil.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var lat: Double = 0.0
    private var lon: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateUI()

        /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if(item.itemId == R.id.profile_mostrar) {
          }
         }*/

        binding.profileMostrar.setOnClickListener {
            binding.profileMostrar.text = "Lat: $lat, Long: $lon"
            //startActivity(Intent(this, EditActivity::class.java))
        }
    }

    private fun updateUI(
        name: String = "Curso UV",
        correo: String = "frivera@uv.com.mx",
        web: String = "https://miuv.com/frivera",
        phone: String = "+52 2291406365"
    ) {
        binding.profileTvNombre.text = name
        binding.profileTvCorreo.text = correo
        binding.profileTvWeb.text = web
        binding.profileTvPhone.text = phone
        //lat = 19.165493348694696
        //lon = -96.11419798183913
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_edit) {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra(getString(R.string.k_name), binding.profileTvNombre.text.toString())
            intent.putExtra(getString(R.string.k_email), binding.profileTvCorreo.toString())
            intent.putExtra(getString(R.string.k_web), binding.profileTvWeb.toString())
            intent.putExtra(getString(R.string.k_phone), binding.profileTvPhone.toString())
            intent.putExtra(getString(R.string.k_lat), lat.toString())
            intent.putExtra(getString(R.string.k_lon), lon.toString())

            //startActivity(Intent) <-Solo lanza una vista
            //startActivity(intent) //<- Lanza y espera una respuesta
            editResult.launch(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private val editResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val name = it.data?.getStringExtra(getString(R.string.k_name))
                val correo = it.data?.getStringExtra(getString(R.string.k_email))
                val web = it.data?.getStringExtra(getString(R.string.k_web))
                val phone = it.data?.getStringExtra(getString(R.string.k_phone))
                lat = it.data?.getStringExtra(getString(R.string.k_lat))?.toDouble() ?: 0.0
                lon = it.data?.getStringExtra(getString(R.string.k_lon))?.toDouble() ?: 0.0
                updateUI(name!!, correo!!, web!!, phone!!)
            }
        }
}
    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            if(requestCode==985){
                val name = data?.getStringExtra(getString("k_name"))
                val correo = data?.getStringExtra(getString("k_email"))
                val web = data?.getStringExtra(getString("k_web"))
                val phone = data?.getStringExtra(getString("k_phone"))
                lat = data?.getStringExtra(getString("k_lat"))?.toDouble() ?:0.0
                lon = data?.getStringExtra(getString("k_lon"))?.toDouble() ?:0.0
                updateUI(name!!,correo!!,web!!,phone!!)
            }
        }
    }
    */
