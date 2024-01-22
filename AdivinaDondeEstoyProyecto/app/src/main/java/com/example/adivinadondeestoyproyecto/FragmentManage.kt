package com.example.adivinadondeestoyproyecto

import Modelo.Almacen
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.adivinadondeestoyproyecto.databinding.ActivityFragmentManageBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class FragmentManage : AppCompatActivity() {
    lateinit var binding: ActivityFragmentManageBinding
    private lateinit var firebaseauth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val TAG1 = "JVVM"
    val TAG2 = "KRCC"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentManageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tbMenuReg)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbMenuReg.setNavigationOnClickListener {
            finish()
        }

        this.title = "Nivel "+(Almacen.nivel+1)



        binding.viewPager.adapter = AdaptadorViewPage(this)
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,index->
            tab.text = when(index){
                0->{
                    Almacen.listLeyend[(0+(Almacen.nivel*5))].nombre}
                1->{
                    Almacen.listLeyend[1+(Almacen.nivel*5)].nombre}
                2->{
                    Almacen.listLeyend[2+(Almacen.nivel*5)].nombre}
                3->{
                    Almacen.listLeyend[3+(Almacen.nivel*5)].nombre}
                4->{
                    Almacen.listLeyend[4+(Almacen.nivel*5)].nombre}
                else->{throw Resources.NotFoundException("Posición no encontrada") }
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        if(Almacen.nivel == 0){
            menu?.findItem(R.id.option_2)?.isVisible = false
        }
        if(Almacen.nivel == ((Almacen.listLeyend.size/5)-1).toInt()){
            menu?.findItem(R.id.option_3)?.isVisible = false
        }

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
            R.id.option_2->{
                Almacen.nivel = Almacen.nivel - 1
                recreate()
            }
            R.id.option_3->{
                Almacen.nivel = Almacen.nivel + 1
                recreate()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}