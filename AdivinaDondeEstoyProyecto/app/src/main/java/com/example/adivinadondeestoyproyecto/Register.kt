package com.example.adivinadondeestoyproyecto

import Modelo.Almacen
import Modelo.Users
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.adivinadondeestoyproyecto.databinding.ActivityRegisterBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseauth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tbMenuReg)
        firebaseauth = FirebaseAuth.getInstance()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbMenuReg.setNavigationOnClickListener {
            finish()
        }
        binding.btnRegistroReg.setOnClickListener {
            if (binding.edtCorreoReg.text?.isNotEmpty() == true && binding.edtContrasenaReg.text?.isNotEmpty() == true) {

                firebaseauth.createUserWithEmailAndPassword(
                    binding.edtCorreoReg.text.toString(),
                    binding.edtContrasenaReg.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        irMenuPrincipal(it.result?.user?.email ?: "")
                        Toast.makeText(this, "Se ha creado con exito", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this,"Error en la creacion", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "No se han rellenado todos los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.option_1 -> {
                Toast.makeText(this,"Temporal", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun irMenuPrincipal(email:String, nombre:String = "Usuario"){
        val homeIntent = Intent(this, Main_Menu::class.java).apply {
            putExtra("email",email)
            putExtra("nombre",nombre)
        }
        startActivity(homeIntent)
    }
    override fun onRestart() {
        super.onRestart()
        finish()
    }
}