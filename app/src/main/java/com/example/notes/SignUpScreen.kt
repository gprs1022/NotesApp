package com.example.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notes.databinding.ActivityLoginScreenBinding
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.databinding.ActivitySignUpScreenBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpScreenBinding
    private lateinit var  databaseHelper: NotesDatabaseHelper
   // private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
       // SQLite database
        databaseHelper = NotesDatabaseHelper(this)

        // Firebase auth for google sign IN
       // firebaseAuth = FirebaseAuth.getInstance()


        binding.youHaveAccount.setOnClickListener {
            val intent = Intent(this,LoginScreen::class.java)
            startActivity(intent)
        }
        binding.SignUpButton.setOnClickListener{
           val email = binding.email.text.toString()

            val password = binding.password.text.toString()

            signupDatabase(email,password)

           /* if(email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()){

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        val intent = Intent(this, LoginScreen::class.java)
                        startActivity(intent)
                    }else
                    {
                        Toast.makeText(this, it.exception.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }else{

                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }*/


        }
    }


    private fun signupDatabase(email:String , password:String){
        val insertRowID = databaseHelper.insertUser(email, password)
        if(insertRowID != -1L){
            Toast.makeText(this,"Signup Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,LoginScreen::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show()
        }
    }
}