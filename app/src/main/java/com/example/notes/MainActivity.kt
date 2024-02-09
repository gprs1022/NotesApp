package com.example.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.notes.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var db: NotesDatabaseHelper
    private lateinit var  notesAdapter: NotesAdapter

    private var userEmail: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db= NotesDatabaseHelper(this)


        // firebase auth

        firebaseAuth = FirebaseAuth.getInstance()
        userEmail = firebaseAuth.currentUser?.email ?: "" // Get current user's email

        binding.logOut.setOnClickListener {

            firebaseAuth.signOut()
            startActivity(
                Intent(this, LoginScreen::class.java)
            )
            finish()
        }

        notesAdapter = NotesAdapter(db.getAllNotes(userEmail), this, userEmail) // Pass userEmail to getAllNotes

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = notesAdapter

        binding.addButton.setOnClickListener{

            val intent = Intent(this, AddNoteActivity::class.java)

            startActivity(intent)
        }



    }

    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes(userEmail))
    }


}