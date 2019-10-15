package com.mikelau.notes.data.remote

import com.mikelau.notes.data.models.Note
import retrofit2.http.GET

interface ApiService {

    @GET("notes")
    suspend fun getNotes(): List<Note>

}
