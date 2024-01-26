package com.example.adivinadondeestoyproyecto

import Modelo.Almacen
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.adivinadondeestoyproyecto.databinding.ActivityFragmentManageBinding
import com.example.adivinadondeestoyproyecto.databinding.ActivityInformacionBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.storage
import java.io.File

class Informacion : AppCompatActivity() {
    lateinit var binding: ActivityInformacionBinding
    private lateinit var firebaseauth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val TAG1 = "JVVM"
    val TAG2 = "KRCC"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        var storage = com.google.firebase.Firebase.storage
        var storageRef = storage.reference
        var spaceRef = storageRef.child("leyendas/${Almacen.listLeyend[Almacen.seleccionado].nombre}.jpg")

        val localfile  = File.createTempFile("tempImage","jpg")
        spaceRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imageView4.setImageBitmap(bitmap)
        }.addOnFailureListener{
            Toast.makeText(this,"Algo ha fallado en la descarga", Toast.LENGTH_SHORT).show()
        }

        binding.textView2.text = Almacen.listLeyend[Almacen.seleccionado].nombre

        binding.editTextTextMultiLine.setText(Almacen.listLeyend[Almacen.seleccionado].historia)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        menu?.findItem(R.id.option_1)?.isVisible = false
        menu?.findItem(R.id.option_2)?.isVisible = false
        menu?.findItem(R.id.option_3)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}