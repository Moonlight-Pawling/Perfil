package com.example.perfil

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import com.example.perfil.R.string.k_photo
import com.example.perfil.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var UbicationButtomTest: Int = 0
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    lateinit var profileMainImageSetter: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        profileMainImageSetter =findViewById(R.id.profile_img)

        updateUI()

            binding.profileMostrar.setOnClickListener {
                if (UbicationButtomTest==0) {
                    binding.profileMostrar.text = "Lat: $lat, Lon: $lon"
                    UbicationButtomTest=1
                }
                else if (UbicationButtomTest==1) {
                    binding.profileMostrar.text = "Mostrar mapa"
                    UbicationButtomTest=0
                }
            }

    }

    private fun updatePhoto(k_photo: String){
        val imageUriString = intent.getStringExtra(getString(R.string.k_photo))
        if (imageUriString != null) {
            val imageUri = k_photo.toUri()
            profileMainImageSetter.setImageURI(imageUri)
        }
    }

    private fun updateUI(
        name: String = "Curso UV",
        correo: String = "frivera@uv.com.mx",
        web: String = "https://miuv.com/frivera",
        phone: String = "+52 2291406365")
    {
        binding.profileTvNombre.text = name
        binding.profileTvCorreo.text = correo
        binding.profileTvWeb.text = web
        binding.profileTvPhone.text = phone
        //lat = 19.165493348694696
        //lon = -96.11419798183913
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_edit) {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra(getString(R.string.k_photo), getString(R.string.k_photo).toUri())
            intent.putExtra(getString(R.string.k_name), binding.profileTvNombre.text.toString().trim())
            intent.putExtra(getString(R.string.k_email), binding.profileTvCorreo.text.toString().trim())
            intent.putExtra(getString(R.string.k_web), binding.profileTvWeb.text.toString().trim())
            intent.putExtra(getString(R.string.k_phone), binding.profileTvPhone.text.toString().trim())
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
                updatePhoto(getString(R.string.k_photo))
            }
        }
}
