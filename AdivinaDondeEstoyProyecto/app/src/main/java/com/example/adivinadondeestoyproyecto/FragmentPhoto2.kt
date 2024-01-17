package com.example.adivinadondeestoyproyecto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adivinadondeestoyproyecto.databinding.FragmentPhoto2Binding


class FragmentPhoto2 : Fragment() {
    lateinit var  binding : FragmentPhoto2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoto2Binding.inflate(inflater, container, false)
        return binding.root
    }


}