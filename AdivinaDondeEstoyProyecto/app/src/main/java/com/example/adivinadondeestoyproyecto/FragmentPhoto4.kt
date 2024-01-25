package com.example.adivinadondeestoyproyecto

import Modelo.Almacen
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.adivinadondeestoyproyecto.databinding.FragmentPhoto3Binding
import com.example.adivinadondeestoyproyecto.databinding.FragmentPhoto4Binding
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.File

class FragmentPhoto4 : Fragment() {
    lateinit var  binding : FragmentPhoto4Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPhoto4Binding.inflate(inflater, container, false)
        var storage = Firebase.storage
        var storageRef = storage.reference
        var spaceRef = storageRef.child("leyendas/${Almacen.listLeyend[3+(Almacen.nivel*5)].nombre}.jpg")

        if(Almacen.listLeyend[3+(Almacen.nivel*5)].acertado){
            binding.textView3.text = "INFORMACION"
        }

        val localfile  = File.createTempFile("tempImage","jpg")
        spaceRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imageView2.setImageBitmap(bitmap)
        }.addOnFailureListener{
            Toast.makeText(context,"Algo ha fallado en la descarga", Toast.LENGTH_SHORT).show()
        }
        binding.textView3.setOnClickListener(){
            Almacen.seleccionado = 3+(Almacen.nivel*5)
            if(Almacen.listLeyend[3+(Almacen.nivel*5)].acertado){
                val MapSapinIntent = Intent(context, Informacion::class.java).apply {}
                startActivity(MapSapinIntent)
            }else{
                val MapSapinIntent = Intent(context, Busqueda::class.java).apply {}
                startActivity(MapSapinIntent)
            }
        }
        return binding.root
    }
}