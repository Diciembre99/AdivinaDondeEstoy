package com.example.adivinadondeestoyproyecto

import Modelo.Almacen
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.adivinadondeestoyproyecto.databinding.FragmentPhoto1Binding
import com.google.firebase.storage.storage
import com.google.firebase.Firebase
import java.io.File

class FragmentPhoto1 : Fragment() {
    lateinit var  binding :FragmentPhoto1Binding

    override fun onResume() {
        super.onResume()
        inicioVideo()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPhoto1Binding.inflate(inflater, container, false)
        var storage = Firebase.storage
        inicioVideo()
        var storageRef = storage.reference
        var spaceRef = storageRef.child("leyendas/${Almacen.listLeyend[0+(Almacen.nivel*5)].nombre}.jpg")
        if(Almacen.listLeyend[0+(Almacen.nivel*5)].acertado){
            binding.textView3.text = "INFORMACION"
        }

        val localfile  = File.createTempFile("tempImage","jpg")
        spaceRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imageView2.setImageBitmap(bitmap)
        }.addOnFailureListener{
            Toast.makeText(context,"Algo ha fallado en la descarga", Toast.LENGTH_SHORT).show()
        }

        binding.imageView2.setOnClickListener(){
            Almacen.seleccionado = 0+(Almacen.nivel*5)

            if(Almacen.listLeyend[0+(Almacen.nivel*5)].acertado){
                val MapSapinIntent = Intent(context, Informacion::class.java).apply {}
                startActivity(MapSapinIntent)
            }else{
                val MapSapinIntent = Intent(context, Busqueda::class.java).apply {}
                startActivity(MapSapinIntent)
            }
        }

        binding.textView3.setOnClickListener(){
            //Almacen.leyend = Almacen.listLeyend[0+(Almacen.nivel*5)]
            Almacen.seleccionado = 0+(Almacen.nivel*5)

            if(Almacen.listLeyend[0+(Almacen.nivel*5)].acertado){
                val MapSapinIntent = Intent(context, Informacion::class.java).apply {}
                startActivity(MapSapinIntent)
            }else{
                val MapSapinIntent = Intent(context, Busqueda::class.java).apply {}
                startActivity(MapSapinIntent)
            }
        }
        return binding.root
    }
    fun inicioVideo(){
        val packageName = requireContext().packageName
        binding.video!!.setVideoURI(
            Uri.parse("android.resource://"
                    + packageName + "/" + R.raw.fondoapp))
        binding.video.start()
        binding.video.setOnCompletionListener { mediaPlayer ->
            mediaPlayer.start()
        }
    }
}