package com.mikelau.notes.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.mikelau.notes.data.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NoteLocalRepository(application: Application) {

    private val database = NoteDatabase.getInstance(application)
    private val noteDao: NoteDao
    private val allNotes: LiveData<List<Note>>

    init {
        noteDao = database.noteDao()
        allNotes = noteDao.allNotes
    }

    fun insert(note: Note) = GlobalScope.launch(Dispatchers.IO) { noteDao.insert(note) }

    fun update(note: Note) = GlobalScope.launch(Dispatchers.IO) { noteDao.update(note) }

    fun delete(note: Note) = GlobalScope.launch(Dispatchers.IO) { noteDao.delete(note) }

    fun deleteAllNotes() = GlobalScope.launch(Dispatchers.IO) { noteDao.deleteAllNotes() }

    fun getAllNotes(): LiveData<List<Note>> { return allNotes }

    fun close() { if (database.isOpen) database.close() }

}
