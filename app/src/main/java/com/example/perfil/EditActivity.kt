package com.example.perfil

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.perfil.databinding.ActivityEditBinding


class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    lateinit var changephotobutton: Button
    lateinit var deletephotobutton: Button
    lateinit var Imageviewineditactivity: ImageView
    private var uri: Uri? = null
    private var uriString: String? = null
    private var restoreUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etnombre.setText(intent.extras?.getString(getString(R.string.k_name)))
        binding.etcorreo.setText(intent.extras?.getString(getString(R.string.k_email)))
        binding.etsitioweb.setText(intent.extras?.getString(getString(R.string.k_web)))
        binding.etphone.setText(intent.extras?.getString(getString(R.string.k_phone)))
        binding.etlat.setText(intent.extras?.getString(getString(R.string.k_lat)).toString())
        binding.etlon.setText(intent.extras?.getString(getString(R.string.k_lon)).toString())

        changephotobutton = findViewById(R.id.changephotobutton)
        deletephotobutton = findViewById(R.id.deletephotobutton)
        Imageviewineditactivity = findViewById(R.id.edit_profile_picture)

        changephotobutton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, 1)
        }

        // Recuperar estado si existe
        var uri = savedInstanceState?.getParcelable<Uri>("restoreUri")
        uri?.let {
            Imageviewineditactivity.setImageURI(it)
        }



        /*
        uriString = intent.getStringExtra(getString(R.string.img_to_editactivity))
        if (uriString != null) {
            uri = Uri.parse(uriString)
            Imageviewineditactivity.setImageURI(uri)
        }

         */
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && data != null) {
            uri = data.data // Assuming you have a 'uri' property declared
            Imageviewineditactivity.setImageURI(uri)
            }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Guardar estado
            outState.putParcelable("restoreUri", uri)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Recuperar estado
        uri = savedInstanceState.getParcelable<Uri>("restoreUri")
        if (uri != null) {
            Imageviewineditactivity.setImageURI(uri)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit,menu)
        return super.onCreateOptionsMenu(menu)
    }





    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_save) {
            /*if (DataValidation()==true) {
                sendData()
            }*/
            sendData()
        }

        return super.onOptionsItemSelected(item)
    }



    fun sendData() {
        val intent = Intent()
        intent.putExtra(getString(R.string.img_to_mainactivity), uri?.toString())
        intent.putExtra(getString(R.string.k_name), binding.etnombre.text.toString())
        intent.putExtra(getString(R.string.k_email), binding.etcorreo.text.toString())
        intent.putExtra(getString(R.string.k_web), binding.etsitioweb.text.toString())
        intent.putExtra(getString(R.string.k_phone), binding.etphone.text.toString())
        intent.putExtra(getString(R.string.k_lat), binding.etlat.text.toString())
        intent.putExtra(getString(R.string.k_lon), binding.etlon.text.toString())
        setResult(RESULT_OK,intent)
        finish()
    }

    private fun DataValidation(): Boolean{
        var ValidationBooleanStatus = true

        //Name Validation
        var nombre = binding.etnombre.text.toString().trim()

        if (nombre.length<3 && nombre.isNotEmpty()){
            binding.boxNombre.run {
                error = "El nombre debe contener al menos 3 caracteres"
                requestFocus()
            }
            ValidationBooleanStatus = false
        }
        else if (nombre.isEmpty()){
            binding.boxNombre.run {
                error = "El nombre no puede estar vacío"
                requestFocus()
            }
            ValidationBooleanStatus = false
        } else {
            binding.boxNombre.error = null
        }

        //eMail validation
        if (Patterns.EMAIL_ADDRESS.matcher(binding.etcorreo.text.toString()).matches() && binding.etcorreo.text.toString().isNotEmpty()){
            binding.boxCorreo.error = null
        } else if (binding.etcorreo.text.isNullOrEmpty()){
            binding.boxCorreo.run{
                error = "El correo no puede estar vacío"
                requestFocus()
            }
            ValidationBooleanStatus = false
        } else {
            binding.boxCorreo.run{
                error = "Formato inválido. " +
                        "Ej: algo.algo@servicio.dominio"
                requestFocus()
            }
            ValidationBooleanStatus = false
        }

        //Website validation
        if (Patterns.WEB_URL.matcher(binding.etsitioweb.text.toString()).matches() && binding.etsitioweb.text.toString().isNotEmpty()){
            binding.boxWeb.error = null
        } else if (binding.etsitioweb.text.isNullOrEmpty()){
            binding.boxWeb.run{
                error = "El sitio web no puede estar vacío"
                requestFocus()
            }
            ValidationBooleanStatus = false
        } else {
            binding.boxWeb.run{
                error = "Formato inválido. " +
                        "Ej: https://miuv.com/frivera"
                requestFocus()
            }
            ValidationBooleanStatus = false
        }

        //Phone validation
        var telephonicnumber = binding.etphone.text.toString().trim()
        if (Patterns.PHONE.matcher(binding.etphone.text.toString()).matches() && binding.etphone.text.toString().isNotEmpty() && telephonicnumber.length>=7){
            binding.boxContacto.error = null
        } else if (binding.etphone.text.isNullOrEmpty()){
            binding.boxContacto.run{
                error = "El teléfono no puede estar vacío"
                requestFocus()
            }
            ValidationBooleanStatus = false
        } else if (telephonicnumber.length<7){
            binding.boxContacto.run{
                error = "7 dígitos para telefono fijo. 10 para celular."
                requestFocus()
            }
            ValidationBooleanStatus = false
        } else {
            binding.boxContacto.run{
                error = "Formato inválido. " +
                        "Ej: +52 2291590846"
                requestFocus()
            }
            ValidationBooleanStatus = false
        }

        //Lat-Lon validation
        if (binding.etlat.text.toString().isEmpty()){
            binding.boxLat.run{
                error = "La latidud no puede estar vacía"
                requestFocus()
            }
            ValidationBooleanStatus = false
        } else {
            binding.boxLat.error = null
        }

        if (binding.etlon.text.toString().isEmpty()){
            binding.boxLon.run{
                error = "La longitud no puede estar vacía"
                requestFocus()
            }
            ValidationBooleanStatus = false
        } else {
            binding.boxLon.error = null
        }

        return ValidationBooleanStatus
    }

}

