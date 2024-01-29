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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseauth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val db = Firebase.firestore
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
        this.title = "Registro"
        binding.btnRegistroReg.setOnClickListener {
            if (binding.edtCorreoReg.text?.isNotEmpty() == true && binding.edtContrasenaReg.text?.isNotEmpty() == true) {

                firebaseauth.createUserWithEmailAndPassword(
                    binding.edtCorreoReg.text.toString(),
                    binding.edtContrasenaReg.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        guardarUsuario()
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
        menu?.findItem(R.id.option_1)!!.isVisible = false
        menu.findItem(R.id.option_2)!!.isVisible = false
        menu.findItem(R.id.option_3)!!.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.option_4->{
                val inflater = layoutInflater
                val build = MaterialAlertDialogBuilder(this)
                val dialogView = inflater.inflate(R.layout.dialog_info, null)
                build.setView(dialogView)
                build.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun irMenuPrincipal(){
        val homeIntent = Intent(this, Main_Menu::class.java)
        startActivity(homeIntent)
    }

    fun guardarUsuario(){
        var user = hashMapOf(
            "email" to binding.edtCorreoReg.text.toString(),
            "score" to 0,
            "tries" to 0
        )

        // Si no existe el documento lo crea, si existe lo remplaza.
        db.collection("users")
            .document(user.get("email").toString()) //Será la clave del documento.
            .set(user).addOnSuccessListener {
            }.addOnCompleteListener{
                Almacen.user = Users(user["usuario"].toString(),user["score"].toString().toInt(),user["tries"].toString().toInt())
                irMenuPrincipal()
            }

    }

    override fun onRestart() {
        super.onRestart()
        finish()
    }
}