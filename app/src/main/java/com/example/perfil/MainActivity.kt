package com.example.perfil

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.perfil.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    public var UbicationButtomTest: Int = 0
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private lateinit var receiveimage: ImageView
    private var uriString: String? = null
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)
        updateUI()

//            binding.profileMostrar.setOnClickListener {
//                if (UbicationButtomTest==0) {
//                    binding.profileMostrar.text = "Lat: $lat, Lon: $lon"
//                    UbicationButtomTest=1
//                }
//                else if (UbicationButtomTest==1) {
//                    binding.profileMostrar.text = "Mostrar mapa"
//                    UbicationButtomTest=0
//                }
//            }

        receiveimage = findViewById(R.id.main_profile_picture)
        lat = 19.16571861761356
        lon = -96.11419823223545
        setupIntent()
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

    private fun setupIntent(){
        binding.profileTvNombre.setOnClickListener{
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(SearchManager.QUERY,binding.profileTvNombre.text)
            }
            startActivity(intent)
        }

        //Envio de correo
        binding.profileTvCorreo.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL,binding.profileTvCorreo.text.toString())
                putExtra(Intent.EXTRA_SUBJECT,"Automatico INTENT")
                putExtra(Intent.EXTRA_TEXT,"Some text here")
            }
            startActivity(intent)
        }

        //Sitio Web
        binding.profileTvWeb.setOnClickListener{
            val webText = Uri.parse(binding.profileTvWeb.text.toString())
            val intent = Intent(Intent.ACTION_VIEW,webText)
            startActivity(intent)
        }

        //Telefono
        binding.profileTvPhone.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL).apply {
                val phone = (it as TextView).text
                data = Uri.parse("tel:$phone")
            }
            startActivity(intent)
        }

        //Mapa
        binding.profileMostrar.setOnClickListener{
            // Crea una cadena Uri a partir de una cadena de intención. Usa el resultado para crear una intención.
            val mapcordinates : String = "$lat,$lon"
            val gmmIntentUri = Uri.parse("geo:$lat,$lon?q=$mapcordinates?z=17")
            // Crea una intención a partir de gmmIntentUri. Establece la acción en ACTION_VIEW
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            // Haz que la intención sea explícita configurando el paquete de Google Maps
            mapIntent.setPackage("com.google.android.apps.maps")
            // Intenta iniciar una actividad que pueda manejar la intención
            startActivity(mapIntent)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_edit) {
            val intent = Intent(this, EditActivity::class.java)
            val uriString = uri.toString()
            intent.putExtra(getString(R.string.img_to_editactivity), uriString)
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
                uriString = it.data?.getStringExtra(getString(R.string.img_to_mainactivity)) // Get the Uri string
                if (uriString != null) {
                    val uri = Uri.parse(uriString)  // Recreate the Uri
                    receiveimage.setImageURI(uri)
                    uriString = uri.toString()
                }
                updateUI(name!!, correo!!, web!!, phone!!)
            }
        }
}
