package com.mikelau.notes.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mikelau.notes.data.models.Note

@Dao
interface NoteDao {

    @get:Query("SELECT * FROM note_table ORDER BY priority DESC")
    val allNotes: LiveData<List<Note>>

    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

}
