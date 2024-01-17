package com.example.adivinadondeestoyproyecto

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adivinadondeestoyproyecto.databinding.ActivityFragmentManageBinding
import com.google.android.material.tabs.TabLayoutMediator

class FragmentManage : AppCompatActivity() {
    lateinit var binding: ActivityFragmentManageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentManageBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
}