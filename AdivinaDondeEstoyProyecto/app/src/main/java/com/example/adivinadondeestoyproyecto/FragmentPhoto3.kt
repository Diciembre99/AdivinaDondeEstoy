package com.example.adivinadondeestoyproyecto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adivinadondeestoyproyecto.databinding.FragmentPhoto1Binding
import com.example.adivinadondeestoyproyecto.databinding.FragmentPhoto3Binding

class FragmentPhoto3 : Fragment() {
    lateinit var  binding : FragmentPhoto3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoto3Binding.inflate(inflater, container, false)
        return binding.root
    }
}