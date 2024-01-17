package com.example.adivinadondeestoyproyecto

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdaptadorViewPage(fragments: FragmentManage): FragmentStateAdapter(fragments) {
    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{FragmentPhoto1()}
            1->{FragmentPhoto2()}
            2->{FragmentPhoto3()}
            3->{FragmentPhoto4()}
            4->{FragmentPhoto5()}
            else->{throw Resources.NotFoundException("Posicion no encontrada")
            }
        }
    }

}