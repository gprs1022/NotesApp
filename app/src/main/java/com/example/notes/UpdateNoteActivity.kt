package com.example.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.notes.databinding.ActivityUpdateNoteBinding

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityUpdateNoteBinding
    private lateinit var db: NotesDatabaseHelper
   private var noteId: Int = -1
    private var userId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        noteId = intent.getIntExtra("note_id", -1)
        userId = intent.getIntExtra("user_id", -1)


        if(noteId == -1 || userId == -1 ){
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        binding.UpdateTitleEditText.setText(note.title)
        binding.UpdateContentEditText.setText(note.content)

        binding.UpdateSaveButton.setOnClickListener {

            val newTitle= binding.UpdateTitleEditText.text.toString()
            val newContent = binding.UpdateContentEditText.text.toString()
            db.updateNote(noteId, newTitle,newContent)


            Toast.makeText(this,"Changes Saved", Toast.LENGTH_SHORT).show()
        }

    }
}