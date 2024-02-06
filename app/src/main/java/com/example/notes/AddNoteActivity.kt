package com.example.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.notes.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db : NotesDatabaseHelper
    //private var userId: Int = -1
    private lateinit var userEmail: String // userEmail should be initialized or passed from intent


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)
// Retrieve userEmail from intent

        binding.saveButton.setOnClickListener {

            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
           Log.d("savebutton", " Error")
            db.insertNote(userEmail, title,content)

            Toast.makeText(this,"Note Saved", Toast.LENGTH_SHORT).show()
        }
    }
}