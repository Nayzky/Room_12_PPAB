package com.example.room.database

import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

//setiap action yang dilewati database diperantarakan oleh notedao
@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @get:Query("SELECT * from note_table ORDER BY id ASC")
    val allNotes: LiveData<List<Note>>

    @Query("DELETE FROM note_table WHERE id = :noteId")
    fun deleteById(noteId: Int)
}