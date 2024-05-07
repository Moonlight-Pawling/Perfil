package com.example.perfil

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import com.example.perfil.databinding.ActivityEditBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.regex.Pattern.matches


class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    lateinit var changephotobutton: Button
    lateinit var deletephotobutton: Button
    lateinit var Imageviewineditactivity: ImageView
    private lateinit var imageFile: File
    private val fileName = "official_profile_image.png"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Imageviewineditactivity = findViewById(R.id.edit_profile_picture)

        val externalStorageDirectory = Environment.getExternalStorageDirectory()
        val imageDirectory = File(externalStorageDirectory, "Pictures")
        imageFile = File(imageDirectory, fileName)
        setImageViewFromLocalFile()
        setFocusLats()

        binding.etnombre.setText(intent.extras?.getString(getString(R.string.k_name)))
        binding.etcorreo.setText(intent.extras?.getString(getString(R.string.k_email)))
        binding.etsitioweb.setText(intent.extras?.getString(getString(R.string.k_web)))
        binding.etphone.setText(intent.extras?.getString(getString(R.string.k_phone)))
        binding.etlat.setText(intent.extras?.getString(getString(R.string.k_lat)).toString())
        binding.etlon.setText(intent.extras?.getString(getString(R.string.k_lon)).toString())

        changephotobutton = findViewById(R.id.changephotobutton)
        deletephotobutton = findViewById(R.id.deletephotobutton)

        changephotobutton.setOnClickListener {
            pickImage()
        }

        deletephotobutton.setOnClickListener {
            removeImage()
            Imageviewineditactivity.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.img_avatar, null))
        }
    }

    private fun removeImage(){
        val externalStorageDirectory = Environment.getExternalStorageDirectory()
        val imageDirectory = File(externalStorageDirectory, "Pictures")
        val imageFile = File(imageDirectory, fileName)
        if (imageFile.exists()) {
            imageFile.delete()
            MediaScannerConnection.scanFile(this, arrayOf(imageFile.absolutePath), null, null)
            Toast.makeText(this, "Imagen eliminada con éxito", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No hay imagen para eliminar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFocusLats() {
        binding.etnombre.setOnFocusChangeListener { view, isFocused ->
            if (isFocused) {
                binding.etnombre.text?.let { binding.etnombre.setSelection(it.length) }
            }
        }

        binding.etcorreo.setOnFocusChangeListener { view, isFocused ->
            if (isFocused) {
                binding.etcorreo.text?.let { binding.etcorreo.setSelection(it.length) }
            }
        }

        binding.etsitioweb.setOnFocusChangeListener { view, isFocused ->
            if (isFocused) {
                binding.etsitioweb.text?.let { binding.etsitioweb.setSelection(it.length) }
            }
        }

        binding.etphone.setOnFocusChangeListener { view, isFocused ->
            if (isFocused) {
                binding.etphone.text?.let { binding.etphone.setSelection(it.length) }
            }
        }

        binding.etlat.setOnFocusChangeListener { view, isFocused ->
            if (isFocused) {
                binding.etlat.text?.let { binding.etlat.setSelection(it.length) }
            }
        }

        binding.etlon.setOnFocusChangeListener { view, isFocused ->
            if (isFocused) {
                binding.etlon.text?.let { binding.etlon.setSelection(it.length) }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit,menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun checkForExistingImage(fileName: String): Boolean {
        val externalStorageDirectory = Environment.getExternalStorageDirectory()
        val imageDirectory = File(externalStorageDirectory, "Pictures")

        val imageFile = File(imageDirectory, fileName)
        return imageFile.exists()
    }
    private fun setImageViewFromLocalFile() { val doesExist = checkForExistingImage(fileName)
            if (doesExist) {
                try {
                    val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                    Imageviewineditactivity.setImageBitmap(bitmap)
                } catch (e: FileNotFoundException) {
                    // Handle the case where the file doesn't exist (e.g., show a toast)
                    e.printStackTrace()
                }
            } else {
                binding.editProfilePicture.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.img_avatar, null))
            }
    }
    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickMediaLauncher.launch(intent)
    }

    private val pickMediaLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imageUri = data?.data // Use safe call to handle null data
            if (imageUri != null) {
                saveImage(imageUri)
            } else {
                // Handle the case where no image was selected (e.g., user canceled)
            }
        }
    }

    private fun saveImage(imageUri: Uri) {
        try {

            // Actualizar la galería del dispositivo para mostrar la imagen guardada
            MediaScannerConnection.scanFile(this, arrayOf(imageFile.absolutePath), null, null)
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            // Crear un nombre de archivo único para la imagen
            val fileName = "official_profile_image.png"

            // Obtener la carpeta de almacenamiento de imágenes
            val externalStorageDirectory = Environment.getExternalStorageDirectory()
            val imageDirectory = File(externalStorageDirectory, "Pictures")
            if (!imageDirectory.exists()) {
                imageDirectory.mkdirs() // Create the directory if it doesn't exist
            }

            // Crear el archivo de imagen y escribir el bitmap
            val imageFile = File(imageDirectory, fileName)
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            // Actualizar la galería del dispositivo para mostrar la imagen guardada
            MediaScannerConnection.scanFile(this, arrayOf(imageFile.absolutePath), null, null)


            // Mostrar un mensaje de éxito
            Toast.makeText(this, "Imagen guardada con éxito", Toast.LENGTH_SHORT).show()
        } catch (e: FileNotFoundException) {
            // Manejar la excepción de archivo no encontrado
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            // Manejar la excepción de E/S
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
        }
        setImageViewFromLocalFile()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_save) {
            if (DataValidation()==true) {
                sendData()
            }
            //sendData()
        }
        return super.onOptionsItemSelected(item)
    }



    fun sendData() {
        val intent = Intent()
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

        if (binding.etlat.text.toString().matches(Regex("^(-?[0-9]+)(\\.[0-9]+)\$")) && binding.etlat.text.toString().isNotEmpty() && (binding.etlat.text.toString().toDouble() <= 90 && binding.etlat.text.toString().toDouble() >= -90)){
            binding.boxLat.error = null
        } else if (binding.etlat.text.isNullOrEmpty()){
            binding.boxLat.run{
                error = "La latitud no puede estar vacía"
                requestFocus()
            }
            ValidationBooleanStatus = false
        } else if (binding.etlat.text.toString().toDouble() > 90 || binding.etlat.text.toString().toDouble() < -90){
            binding.boxLat.run{
                error = "La latitud debe estar entre -90 y 90"
                requestFocus()
            }
            ValidationBooleanStatus = false
        } else if (!(binding.etlat.text.toString().matches(Regex("^(-?[0-9]+)(\\.[0-9]+)\$")))) {
            binding.boxLat.run {
                error = "La latitud debe ser un número con punto decimal"
                requestFocus()
            }
            ValidationBooleanStatus = false
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

