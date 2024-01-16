package com.example.adivinadondeestoyproyecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
                        goMainMenu(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
                        Toast.makeText(this,"Se inicio Sesion", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Ha habido un error", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "No se han introducido todos los datos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
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
        launcherWindowGoogle.launch(signInClient)
    }

    private val launcherWindowGoogle =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            manageResult(task)
        }
    }

    private fun manageResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
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
                goMainMenu(account.email.toString(), account.displayName.toString())
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

}