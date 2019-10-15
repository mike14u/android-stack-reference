package com.mikelau.notes

import android.app.Application
import com.mikelau.notes.modules.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ApplicationController : Application() {

    override fun onCreate() {
        super.onCreate()

        // Koin
        startKoin {
            androidLogger()
            androidContext(this@ApplicationController)
            modules(appModules)
        }
    }
}
