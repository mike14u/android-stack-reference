package com.mikelau.notes.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.mikelau.notes.data.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NoteLocalRepository(application: Application) {

    private val noteDao: NoteDao
    private val allNotes: LiveData<List<Note>>

    init {
        val database = NoteDatabase.getInstance(application)
        noteDao = database.noteDao()
        allNotes = noteDao.allNotes
    }

    fun insert(note: Note) = GlobalScope.launch(Dispatchers.IO) { noteDao.insert(note) }

    fun update(note: Note) = GlobalScope.launch(Dispatchers.IO) { noteDao.update(note) }

    fun delete(note: Note) = GlobalScope.launch(Dispatchers.IO) { noteDao.delete(note) }

    fun deleteAllNotes() = GlobalScope.launch(Dispatchers.IO) { noteDao.deleteAllNotes() }

    fun getAllNotes(): LiveData<List<Note>> { return allNotes }

}
