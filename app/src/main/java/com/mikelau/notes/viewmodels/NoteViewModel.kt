package com.mikelau.notes.viewmodels

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mikelau.notes.base.BaseViewModel
import com.mikelau.notes.data.local.NoteLocalRepository
import com.mikelau.notes.data.models.Note
import com.mikelau.notes.data.remote.NoteRemoteRepository
import com.mikelau.notes.util.SingleLiveEvent
import com.mikelau.notes.util.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteViewModel(application: Application, private val repository: NoteRemoteRepository) :
    BaseViewModel() {

    var profileName = ObservableField("")
    var profileEmail = ObservableField("")

    private val localRepository: NoteLocalRepository = NoteLocalRepository(application)
    private val allNotes: LiveData<List<Note>>
    private val notesList = MutableLiveData<List<Note>>()

    val showLoading = MutableLiveData<Boolean>()
    val showError = SingleLiveEvent<String>()

    init {
        allNotes = localRepository.getAllNotes()
        loadNotes()
    }

    fun insert(note: Note) = launch(coroutineContext) { localRepository.insert(note) }

    fun update(note: Note) = launch(coroutineContext) { localRepository.update(note) }

    fun delete(note: Note) = launch(coroutineContext) { localRepository.delete(note) }

    fun deleteAllNotes() = launch(coroutineContext) { localRepository.deleteAllNotes() }

    fun closeDb() { localRepository.close() }

    fun getAllNotes(): LiveData<List<Note>> { return allNotes }

    private fun loadNotes() {
        showLoading.value = true
        launch {
            val result = withContext(Dispatchers.IO) { repository.getNotes() }
            showLoading.value = false
            when (result) {
                is UseCaseResult.Success -> notesList.value = result.data
                is UseCaseResult.Error -> showError.value = result.exception.message
            }
        }
    }

    fun saveNote(title: String, description: String, priority: Int) {
        insert(Note(title, description, priority))
    }

    fun modifyNote(title: String, description: String, priority: Int, id: Int) {
        val note = Note(title, description, priority)
        note.id = id
        update(note)
    }

}
