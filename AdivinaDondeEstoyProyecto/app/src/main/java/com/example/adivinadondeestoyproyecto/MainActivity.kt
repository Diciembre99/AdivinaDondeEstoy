package com.example.adivinadondeestoyproyecto

import Modelo.Almacen
import Modelo.Users
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adivinadondeestoyproyecto.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var firebaseauth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    var db = Firebase.firestore
    val TAG1 = "JVVM"
    val TAG2 = "KRCC"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseauth = FirebaseAuth.getInstance()


        binding.btnLogin.setOnClickListener {
            if (binding.edtEmail.text?.isNotEmpty() == true && binding.edtPassword.text?.isNotEmpty() == true ){
                firebaseauth.signInWithEmailAndPassword(binding.edtEmail.text.toString(),binding.edtPassword.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        db.collection("users")
                            .whereEqualTo("email",binding.edtEmail.text.toString())
                            .get()
                            .addOnSuccessListener{
                                for (document in it){
                                    Almacen.user = Users(document.get("email").toString(),document.get("score").toString().toInt(), document.get("tries").toString().toInt())
                                }
                            }.addOnCompleteListener {
                                goMainMenu(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
                            }
                        Toast.makeText(this,"Se inicio Sesion", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Ha habido un error", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "No se han introducido todos los datos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            val registerIntent = Intent(this, Register::class.java)
            startActivity(registerIntent)
        }

        firebaseauth.signOut()
        // Configuracion para el inicio de sesion con google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        // Inicio de sesion con google
        binding.btnGoogle.setOnClickListener {
            Log.d(TAG1,"PRUEBA 4")
            loginEnGoogle()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.option_1 -> {
                Toast.makeText(this,"Temporal", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loginEnGoogle(){
        val signInClient = googleSignInClient.signInIntent
        Log.d(TAG1,"PRUEBA 5")
        launcherWindowGoogle.launch(signInClient)
    }

    private val launcherWindowGoogle =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            Log.d(TAG1,"PRUEBA 6")
            manageResult(task)

        }
    }

    private fun manageResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                Log.d(TAG1,"PRUEBA 7")
                updateUI(account)
            }
        }
        else {
            Toast.makeText(this,task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseauth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this,"Se ha iniciado sesion con google", Toast.LENGTH_SHORT).show()
                guardarUsuario(account.email.toString(), account.displayName.toString())
            }
            else {
                Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goMainMenu(email:String, nombre:String = "Usuario"){
        val homeIntent = Intent(this, Main_Menu::class.java).apply {
            putExtra("email",email)
            putExtra("nombre",nombre)
        }
        startActivity(homeIntent)
    }

    fun guardarUsuario(email: String, gUser: String) {
        var al = ArrayList<String>()
        var user = hashMapOf(
            "email" to email,
            "score" to 0,
            "tries" to 0
        )
        //Users(user["usuario"].toString(),user["roles"].toString().toInt(),user["email"].toString(),user["genero"].toString().toInt(),user["Monedas"].toString().toInt())
        db.collection("users")
            .whereEqualTo("email",email)
            .get()
            .addOnSuccessListener{
                for (document in it){
                    Log.d(TAG1,"PRUEBA 1")
                    al.add(document.data.toString())
                }

                if (al.size == 0){
                    db.collection("users")
                        .document(user["email"].toString())
                        .set(user).addOnSuccessListener {
                            Log.d(TAG1,"PRUEBA 2")
                        }
                }
                else {
                    Log.d(TAG1,"PRUEBA 3")
                    for (document in it){
                        Almacen.user = Users(document.get("email").toString(),document.get("score").toString().toInt(), document.get("tries").toString().toInt())
                    }
                }
            }
            .addOnCompleteListener{
                goMainMenu(email,gUser)
            }
    }
}
