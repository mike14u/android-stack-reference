package com.mikelau.notes.modules

import com.mikelau.notes.data.remote.ApiService
import com.mikelau.notes.data.remote.NoteRemoteRepository
import com.mikelau.notes.data.remote.NoteRemoteRepositoryImpl
import com.mikelau.notes.util.BASE_URL
import com.mikelau.notes.util.LoggingInterceptor
import com.mikelau.notes.viewmodels.NoteViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val appModules = module {
    single {
        createWebService<ApiService>(
            okHttpClient = createHttpClient()
        )
    }
    factory<NoteRemoteRepository> { NoteRemoteRepositoryImpl(get(), get()) }
    viewModel { NoteViewModel(get(), get()) }
}

// Logging Interceptor & Content Type
fun createHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .readTimeout(5 * 60, TimeUnit.SECONDS)
        .addInterceptor(LoggingInterceptor())
        .addInterceptor {
            val original = it.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.header("Content-Type", "application/json")
            val request = requestBuilder.method(original.method, original.body).build()
            return@addInterceptor it.proceed(request)
        }.build()
}

// Retrofit Instance
inline fun <reified T> createWebService(okHttpClient: OkHttpClient): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build()
    return retrofit.create(T::class.java)
}
