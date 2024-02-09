package com.example.notes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.notes.databinding.ActivityLoginScreenBinding
import com.example.notes.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ActionCodeEmailInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginScreen : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var  databaseHelper: NotesDatabaseHelper
    private lateinit var  firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //othe Cient ID
       val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
           .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        //firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        //SQLite Databes
        databaseHelper = NotesDatabaseHelper(this)

        //google signIn


         googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)


        binding.signInGoogle.setOnClickListener {

            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }

        binding.dontHaveAccount.setOnClickListener{

            val intent = Intent(this, SignUpScreen::class.java)
            startActivity(intent)
        }

        binding.LoginButton.setOnClickListener{
            val email = binding.email.text.toString()

            val password = binding.password.text.toString()

            loginDatabase(email,password)
           /* if(email.isNotEmpty() && password.isNotEmpty()){

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else
                    {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{

                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }*/
        }
    }


    private fun loginDatabase(email: String, password: String){

        val userExists = databaseHelper.getUser(email,password)
        if (userExists != null){
            Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java).apply {

            }
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
        }
    }

    //launcher for google sign-in
    private val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    if (task.isSuccessful) {
                        val account: GoogleSignInAccount = task.result
                        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                        // Log the ID token for debugging


                        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                // Successfully sign in with Google
                                startActivity(Intent(this, MainActivity::class.java))
                                Toast.makeText(
                                    this,
                                    "Successfully signed in with Google",
                                    Toast.LENGTH_SHORT
                                ).show()

                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Google Sign-in failed😌:",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                    }

                }
            }
    /*override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser !=null ){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }*/
}
