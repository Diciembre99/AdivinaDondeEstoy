package com.example.adivinadondeestoyproyecto

import Adaptador.AdaptadorPoint
import Modelo.Almacen
import Modelo.Point
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adivinadondeestoyproyecto.databinding.ActivityBusquedaBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.storage
import java.io.File

class Busqueda : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnPoiClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener  {

    lateinit var binding: ActivityBusquedaBinding
    private lateinit var firebaseauth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var miRecyclerView : RecyclerView
    lateinit var miAdapter : AdaptadorPoint
    private var mediaPlayer: MediaPlayer? = null
    val TAG1 = "JVVM"
    val TAG2 = "KRCC"
    var db = Firebase.firestore
    var tries = 5
    lateinit var posicionLeyenda : LatLng
    var purpleMin = 400000f - Almacen.nivel*10000
    var redMin = 300000f - Almacen.nivel*10000
    var orangeMin = 200000f - Almacen.nivel*10000
    var greenMax = 100000f - Almacen.nivel*10000


    private val LOCATION_REQUEST_CODE: Int = 0
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap

    var alMarcadores = ArrayList<Marker>()

    companion object {
        const val REQUEST_CODE_LOCATION = 0
        @SuppressLint("StaticFieldLeak")
        lateinit var contextoPrincipal: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusquedaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tbBusqueda)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbBusqueda.setNavigationOnClickListener {
            finish()
        }
        if (Almacen.nivel == 10){
            purpleMin = 310000f
            redMin = 210000f
            orangeMin = 110000f
            greenMax = 10000f
        }

        posicionLeyenda = LatLng(Almacen.listLeyend[Almacen.seleccionado].cordenadaX,Almacen.listLeyend[Almacen.seleccionado].cordenadaY)
        this.title = "Busqueda"
        lateinit var imagen: Bitmap
        var storage = com.google.firebase.Firebase.storage
        var storageRef = storage.reference
        var spaceRef = storageRef.child("leyendas/${Almacen.listLeyend[Almacen.seleccionado].nombre}.jpg")

        val localfile  = File.createTempFile("tempImage","jpg")
        spaceRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imPista.setImageBitmap(bitmap)
            imagen = bitmap
        }.addOnFailureListener{
            Toast.makeText(this,"Algo ha fallado en la descarga", Toast.LENGTH_SHORT).show()
        }

        binding.tvName.text = Almacen.listLeyend[Almacen.seleccionado].nombre
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)


        binding.imPista.setOnClickListener(){
            val inflater = layoutInflater
            val build = MaterialAlertDialogBuilder(this)
            val dialogView = inflater.inflate(R.layout.dialogpista, null)
            val image: ImageView = dialogView.findViewById(R.id.imgPista)
            image.setImageBitmap(imagen)
            build.setView(dialogView)
            build.show()
        }

        Almacen.listPoint = ArrayList()

        miRecyclerView = binding.recycledView
        miRecyclerView.setHasFixedSize(true)
        miRecyclerView.layoutManager = GridLayoutManager(this, 1)

        miAdapter = AdaptadorPoint(Almacen.listPoint,this)

        miRecyclerView.adapter = miAdapter

        contextoPrincipal = this

    }

    fun calculatedDistance(endPoint: LatLng): Float{
        val result = FloatArray(1)
        Location.distanceBetween(
            posicionLeyenda.latitude, posicionLeyenda.longitude,
            endPoint.latitude, endPoint.longitude,
            result
        )
        return result[0]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        menu?.findItem(R.id.option_1)?.isVisible = false
        menu?.findItem(R.id.option_2)?.isVisible = false
        menu?.findItem(R.id.option_3)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap
        //Se pueden seleccionar varios tiops de mapas:
        //  None --> no muestra nada, solo los marcadores. (MAP_TYPE_NONE)
        //  Normal --> El mapa por defecto. (MAP_TYPE_NORMAL)
        //  Satélite --> Mapa por satélite.  (MAP_TYPE_SATELLITE)
        //  Híbrido --> Mapa híbrido entre Normal y Satélite. (MAP_TYPE_HYBRID) Muestra satélite y mapas de carretera, ríos, pueblos, etc... asociados.
        //  Terreno --> Mapa de terrenos con datos topográficos. (MAP_TYPE_TERRAIN)
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        map.setOnPoiClickListener(this)
        map.setOnMapLongClickListener (this)
        map.setOnMarkerClickListener(this)

        enableMyLocation() //--> Hanilita, pidiendo permisos, la localización actual.
        createMarker()
    }


    //----------------------------------------------------------------------------------------
    @SuppressLint("MissingPermission")
    fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (isPermissionsGranted()) {
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    /**
     * función que usaremos a lo largo de nuestra app para comprobar si el permiso ha sido aceptado o no.
     */
    fun isPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    /**
     * Método que solicita los permisos.
     */
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
        }
    }

    //-----------------------------------------------------------------------------------------------------

    /**
     * Método en el que crearemos algunos marcadores de ejemplo.
     */
    private fun createMarker() {
        val marker = LatLng(38.98491,-3.7038)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(marker, 5.5f),
            4000,
            null
        )
    }
    /**
     * Con este método vamos a ajustar el tamaño de todos los iconos que usemos en los marcadores.
     */
//-----------------------------------------------------------------------------------------------------
    //----------------------------------------- Eventos en el mapa ----------------------------------------
    //-----------------------------------------------------------------------------------------------------

    /**
     * Se dispara cuando pulsamos la diana que nos centra en el mapa (punto negro, arriba a la derecha en forma de diana).
     */
    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    /**
     * Se dispara cuando pulsamos en nuestra localización exacta donde estámos ahora (punto azul).
     */
    override fun onMyLocationClick(p0: Location) {
    }

    /**
     * Con el parámetro podremos obtener información del punto de interés. Este evento se lanza cuando pulsamos en un POI.
     */
    override fun onPoiClick(p0: PointOfInterest) {
    }

    /**
     * Con el parámetro crearemos un marcador nuevo. Este evento se lanzará al hacer un long click en alguna parte del mapa.
     */
    override fun onMapLongClick(p0: LatLng) {
        if(tries != 0 && !Almacen.listLeyend[Almacen.seleccionado].acertado){
            val distancia = calculatedDistance(p0)
            var colorPoint : Drawable? = null

            Log.e(TAG1,distancia.toString())

            var color: Float = 0f

            when{
                distancia > purpleMin->{
                    color = BitmapDescriptorFactory.HUE_VIOLET
                    colorPoint = ContextCompat.getDrawable(this,R.drawable.location_dot_solid_purple)
                }
                purpleMin >= distancia && distancia > redMin ->{
                    color = BitmapDescriptorFactory.HUE_RED
                    colorPoint = ContextCompat.getDrawable(this,R.drawable.location_dot_solid_red)
                }
                redMin >= distancia && distancia > orangeMin ->{
                    color = BitmapDescriptorFactory.HUE_ORANGE
                    colorPoint = ContextCompat.getDrawable(this,R.drawable.location_dot_solid_orange)
                }
                orangeMin >= distancia && distancia > greenMax ->{
                    color = BitmapDescriptorFactory.HUE_YELLOW
                    colorPoint = ContextCompat.getDrawable(this,R.drawable.location_dot_solid_yellow)
                }
                greenMax >= distancia ->{
                    color = BitmapDescriptorFactory.HUE_GREEN
                    colorPoint = ContextCompat.getDrawable(this,R.drawable.location_dot_solid_green)
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(p0, 10.5f),
                        4000,
                        null
                    )
                    Almacen.listLeyend[Almacen.seleccionado].acertado = true
                    mediaPlayer = MediaPlayer.create(this, R.raw.check)
                    val player = mediaPlayer ?: return
                    player.start()
                    Almacen.score = Almacen.score + (500*tries)
                    Almacen.tries = Almacen.tries + (6-tries)
                    dialogFinish("Felicidades, desbloqueaste a este personaje\nConseguiste "+(500*tries)+ " puntos\nLlevas "+Almacen.score+ " puntos","Acertaste")


                }

            }

            Almacen.listPoint.add(Point("",colorPoint,p0.latitude,p0.longitude))
            miAdapter.actualizarDatos()

            var marcador = map.addMarker(
                MarkerOptions().position(p0!!).title("Intento "+(6-tries))
                    .icon(BitmapDescriptorFactory.defaultMarker(color))
            )
            alMarcadores.add(marcador!!)


            if (!Almacen.listLeyend[Almacen.seleccionado].acertado){
                tries--
            }


            when(tries){
                4 ->{
                    binding.imCorazon1.setImageDrawable(getDrawable(R.drawable.corazon_x))
                }
                3 ->{
                    binding.imCorazon2.setImageDrawable(getDrawable(R.drawable.corazon_x))
                }
                2 ->{
                    binding.imCorazon3.setImageDrawable(getDrawable(R.drawable.corazon_x))
                }
                1 ->{
                    binding.imCorazon4.setImageDrawable(getDrawable(R.drawable.corazon_x))
                }
                0 ->{
                    binding.imCorazon5.setImageDrawable(getDrawable(R.drawable.corazon_x))
                    Almacen.score = Almacen.score + -500
                    Almacen.tries = Almacen.tries + tries
                    dialogFinish("Perdiste, vuelve a intentarlo\nPierdes 500 puntos\nLlevas "+Almacen.score+ " puntos","Fallaste")
                }
            }
        }

    }

    fun dialogFinish(message :String, tittle :String){
        MaterialAlertDialogBuilder(this)
            .setTitle(tittle)
            .setMessage(message)
            .setPositiveButton("Aceptar") { dialog, which ->
                finish()
            }
            .show()
    }

    override fun onMarkerClick(p0: Marker): Boolean {

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.run {
            setTitle(p0.title)
            setMessage("\nLatitud: " + p0!!.position.latitude.toString() + " \nLongitud: " + p0.position.longitude.toString())
            setPositiveButton("Aceptar"){ dialog: DialogInterface, i:Int ->
            }
        }
        dialogBuilder.create().show()

        return true;
    }
}