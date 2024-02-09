package com.example.notes

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class NotesDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USERS = "users"
        private const val TABLE_NOTES = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_USER_ID = "user_id"

        private const val TABLE_NAME = "allnotes"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTableQuery =
                "CREATE TABLE $TABLE_USERS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_EMAIL TEXT, $COLUMN_PASSWORD TEXT)"
        db?.execSQL(createUserTableQuery)

        val createNotesTableQuery =
                "CREATE TABLE $TABLE_NOTES ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_USER_ID INTEGER)"
        db?.execSQL(createNotesTableQuery)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)


        val dropNotesTableQuery = "DROP TABLE IF EXISTS $TABLE_NOTES"
        db?.execSQL(dropNotesTableQuery)

        onCreate(db)


    }


    fun insertUser(email: String, password: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
        }
        val userId = db.insert(TABLE_USERS, null, values)
        db.close()
        return userId
    }

    fun getUser(email: String, password: String): User? {
        val db = readableDatabase
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)
        val user: User? = if (cursor.moveToFirst()) {
            User(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            )
        } else {
            null
        }
        cursor.close()
        db.close()
        return user
    }

    fun insertNote(userEmail: String, title: String, content: String): Long {
        val db = writableDatabase
        val userId = getUserIdByEmail(userEmail)



             val values = ContentValues().apply {
                 put(COLUMN_TITLE, title)
                 put(COLUMN_CONTENT, content)
                 put(COLUMN_USER_ID, userId)
             }
           val noteId=  db.insert(TABLE_NOTES, null, values)
           db.close()
        return noteId

    }

    fun updateNote(noteId: Int, title:String,content: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_CONTENT, content)
        }

        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        val rowsUpdate = db.update(TABLE_NOTES, values, whereClause,whereArgs)
        db.close()
        return rowsUpdate
    }

    fun deleteNote(noteId: Int): Int {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        val rowsDeleted = db.delete(TABLE_NOTES, whereClause, whereArgs)
        db.close()
        return rowsDeleted
    }






    fun getAllNotes(userEmail: String): List<Note> {
        val noteList = mutableListOf<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NOTES WHERE $COLUMN_USER_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userEmail))

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

            val note = Note(id, title, content)
            noteList.add(note)
        }

        cursor.close()
        db.close()
        return noteList
    }

    fun getNoteByID(noteId: Int): Note {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NOTES WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

        cursor.close()
        db.close()
        return Note(noteId, title, content)
    }


    fun getUserIdByEmail(email: String): Int? {
        val db = readableDatabase
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val userId: Int? = if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        } else {
            null
        }
        cursor.close()
        db.close()
        return userId
    }


}
