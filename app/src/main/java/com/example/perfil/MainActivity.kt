package com.example.perfil

import android.app.SearchManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import com.example.perfil.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    public var UbicationButtomTest: Int = 0
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    lateinit var Imageviewinmainactivity: ImageView
    var mapbuttonclickactionable: Boolean = false
    private lateinit var imageFile: File
    private val fileName = "official_profile_image.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)
        Imageviewinmainactivity = findViewById(R.id.main_profile_picture)
        updateUI()
        val externalStorageDirectory = Environment.getExternalStorageDirectory()
        val imageDirectory = File(externalStorageDirectory, "Pictures")
        imageFile = File(imageDirectory, fileName)
        setImageViewFromLocalFile()
        lat = 19.16571861761356
        lon = -96.11419823223545
        setupIntent()
    }

    private fun setImageViewFromLocalFile() { val doesExist = checkForExistingImage(fileName)
        if (doesExist) {
                try {
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                Imageviewinmainactivity.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                // Handle the case where the file doesn't exist (e.g., show a toast)
                e.printStackTrace()
            }
        } else {
            binding.mainProfilePicture.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.img_avatar, null))
        }
    }

    private fun checkForExistingImage(fileName: String): Boolean {
        val externalStorageDirectory = Environment.getExternalStorageDirectory()
        val imageDirectory = File(externalStorageDirectory, "Pictures")

        val imageFile = File(imageDirectory, fileName)
        return imageFile.exists()
    }

    private fun updateUI(
        name: String = "Asking to Stack Overflow",
        correo: String = "stack@overflow.com",
        web: String = "https://stackoverflow.com",
        phone: String = "+01 2345678910")

    {
        binding.profileTvNombre.text = name
        binding.profileTvCorreo.text = correo
        binding.profileTvWeb.text = web
        binding.profileTvPhone.text = phone
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun launchIntent(intent: Intent) {
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this,
                getString(R.string.compatible_app_not_found_text), Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupIntent(){
        binding.profileTvNombre.setOnClickListener{
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(SearchManager.QUERY,binding.profileTvNombre.text)
            }
            launchIntent(intent)
        }

        //Envio de correo
        binding.profileTvCorreo.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL,binding.profileTvCorreo.text.toString())
                putExtra(Intent.EXTRA_SUBJECT,"Automatico INTENT")
                putExtra(Intent.EXTRA_TEXT,"Some text here")
            }
            launchIntent(intent)
        }

        //Sitio Web
        binding.profileTvWeb.setOnClickListener{
            val webText = Uri.parse(binding.profileTvWeb.text.toString())
            val intent = Intent(Intent.ACTION_VIEW,webText)
            launchIntent(intent)
        }

        //Telefono
        binding.profileTvPhone.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL).apply {
                val phone = (it as TextView).text
                data = Uri.parse("tel:$phone")
            }
            launchIntent(intent)
        }

        //Mapa
        binding.profileMostrar.setOnClickListener{
            if (mapbuttonclickactionable == false) {
                binding.profileMostrar.text = "Lat: $lat \nLon: $lon"
                mapbuttonclickactionable = true
                // Crea una cadena Uri a partir de una cadena de intención. Usa el resultado para crear una intención.
                val mapcordinates: String = "$lat, $lon"
                val gmmIntentUri = Uri.parse("geo:$lat,$lon?q=$mapcordinates?z=17")
                // Crea una intención a partir de gmmIntentUri. Establece la acción en ACTION_VIEW
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                // Haz que la intención sea explícita configurando el paquete de Google Maps
                mapIntent.setPackage("com.google.android.apps.maps")

                // Intenta iniciar una actividad que pueda manejar la intención
                startActivity(mapIntent)
            } else if (mapbuttonclickactionable == true) {
                mapbuttonclickactionable = false
                binding.profileMostrar.text = "Mostrar Ubicación"
            }
        }
    }


    private suspend fun sleeping(millis: Long) {
            delay(millis)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_edit) {
            val intent = Intent(this, EditActivity::class.java)
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
                setImageViewFromLocalFile()
                updateUI(name!!, correo!!, web!!, phone!!)

            }
        }
}
