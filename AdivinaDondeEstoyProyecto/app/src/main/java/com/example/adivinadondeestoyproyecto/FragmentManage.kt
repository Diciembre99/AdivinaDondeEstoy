package com.example.adivinadondeestoyproyecto

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
        this.title = "Nivel 1"
        binding.viewPager.adapter = AdaptadorViewPage(this)
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,index->
            tab.text = when(index){
                0->{"Foto1"}
                1->{"Foto2"}
                2->{"Foto3"}
                3->{"Foto4"}
                4->{"Foto5"}
                else->{throw Resources.NotFoundException("Posición no encontrada") }
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
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
}