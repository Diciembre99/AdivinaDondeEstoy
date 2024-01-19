package com.example.adivinadondeestoyproyecto

import Modelo.Almacen
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.adivinadondeestoyproyecto.databinding.FragmentPhoto2Binding
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.File


class FragmentPhoto2 : Fragment() {
    lateinit var  binding : FragmentPhoto2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoto2Binding.inflate(inflater, container, false)
        var storage = Firebase.storage
        var storageRef = storage.reference
        var spaceRef = storageRef.child("leyendas/${Almacen.listLeyend[1].nombre}.jpg")

        val localfile  = File.createTempFile("tempImage","jpg")
        spaceRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imageView2.setImageBitmap(bitmap)
        }.addOnFailureListener{
            Toast.makeText(context,"Algo ha fallado en la descarga", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }


}