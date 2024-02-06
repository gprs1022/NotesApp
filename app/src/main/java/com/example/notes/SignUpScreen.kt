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

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        binding.youHaveAccount.setOnClickListener {
            val intent = Intent(this,LoginScreen::class.java)
            startActivity(intent)
        }
        binding.SignUpButton.setOnClickListener{
           val email = binding.email.text.toString()
            val name = binding.email.text.toString()
            val password = binding.password.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()){

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
            }


        }
    }
}