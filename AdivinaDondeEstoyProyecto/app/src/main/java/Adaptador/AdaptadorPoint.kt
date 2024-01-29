package Adaptador

import Modelo.Point
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adivinadondeestoyproyecto.R

class AdaptadorPoint (var listPoint : ArrayList<Point>, var  context: Context) : RecyclerView.Adapter<AdaptadorPoint.ViewHolder>() {
    val TAG = "JVVM"

    companion object {
        var seleccionado: Int = -1
    }
    /**
     * Se encarga de darno la posicion del elemento seleccionado
     * */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPoint[position]
        holder.bind(item, context, position, this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.poing_card, parent, false)
        val viewHolder = ViewHolder(vista)

        return viewHolder
    }
    override fun getItemCount(): Int {
        return listPoint.size
    }
    /**
     * Clase encargada de la vista de cada uno de los elementos del recyclerView
     * Tambien asignamos lo que realiza al pulsar sobre una de las cartas
     * */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Elementos que componen cada carta
        val pointImage = view.findViewById(R.id.imageView5) as ImageView
        val distancia = view.findViewById(R.id.tvDistancia) as TextView



        @SuppressLint("ResourceAsColor")
        fun bind(
            point: Point,
            context: Context,
            pos: Int,
            miAdaptadorRecycler: AdaptadorPoint
        ) {
            pointImage.setImageDrawable(point.punto)
            distancia.text = "Distancia: "+((point.distance /1000).toInt()).toString()+" KM"
        }
    }
    fun actualizarDatos() {
        notifyDataSetChanged()
    }
}