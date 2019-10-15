package com.mikelau.notes.data.remote

import android.app.Application
import com.mikelau.notes.data.local.NoteDao
import com.mikelau.notes.data.local.NoteDatabase
import com.mikelau.notes.data.models.Note
import com.mikelau.notes.util.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface NoteRemoteRepository {
    suspend fun getNotes(): UseCaseResult<List<Note>>
}

class NoteRemoteRepositoryImpl(private val application: Application,
                               private val api: ApiService) : NoteRemoteRepository {
    override suspend fun getNotes(): UseCaseResult<List<Note>> {
        return try {
            val result = api.getNotes()
            populateDatabaseAsync(application, result)
            UseCaseResult.Success(result)
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }

    private fun populateDatabaseAsync(application: Application, notes: List<Note>) = GlobalScope.launch(Dispatchers.IO) {
        val database = NoteDatabase.getInstance(application)
        val noteDao: NoteDao = database.noteDao()
        for(x in notes) noteDao.insert(x)
    }
}