package com.example.adivinadondeestoyproyecto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adivinadondeestoyproyecto.databinding.FragmentPhoto1Binding

class FragmentPhoto1 : Fragment() {
    lateinit var  binding :FragmentPhoto1Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoto1Binding.inflate(inflater, container, false)
        return binding.root
    }
}