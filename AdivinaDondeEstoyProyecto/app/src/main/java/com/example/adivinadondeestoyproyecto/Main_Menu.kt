package com.example.adivinadondeestoyproyecto

import Modelo.Almacen
import Modelo.Leyend
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.adivinadondeestoyproyecto.databinding.ActivityMainMenuBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Main_Menu : AppCompatActivity() {
    lateinit var binding: ActivityMainMenuBinding
    private lateinit var firebaseauth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val TAG1 = "JVVM"
    val TAG2 = "KRCC"
    var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tbMenuMain)
        firebaseauth = FirebaseAuth.getInstance()
        binding.btnMapaSpain.setOnClickListener {
            //val MapSapinIntent = Intent(this, FragmentManage::class.java).apply {}
            Almacen.listLeyend =  ArrayList()
            db.collection("leyendas")
                .get()
                .addOnSuccessListener {
                    for (document in it){
                        Almacen.listLeyend.add(Leyend(document.get("nombre").toString(),document.get("cordeX").toString().toDouble(),document.get("cordeY").toString().toDouble(),document.get("descripcion").toString(),false))
                    }
                    Almacen.listLeyend.shuffle()

                }
                .addOnCompleteListener{
                    Almacen.nivel = 0
                    val MapSapinIntent = Intent(this, FragmentManage::class.java).apply {}
                    startActivity(MapSapinIntent)
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        menu?.findItem(R.id.option_2)?.isVisible = false
        menu?.findItem(R.id.option_3)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.option_1 -> {
                Log.e(TAG1, firebaseauth.currentUser.toString())
                firebaseauth.signOut()

                val signInClient = Identity.getSignInClient(this)
                signInClient.signOut()
                Log.e(TAG1,"Cerrada sesión completamente")
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRestart() {
        super.onRestart()
        if (firebaseauth.currentUser.toString() == "null"){
            finish()
        }
        recreate()
    }
}