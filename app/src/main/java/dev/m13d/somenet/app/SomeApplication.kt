package dev.m13d.somenet.app

import android.app.Application
import org.koin.core.context.startKoin

class SomeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(applicationModule)
        }
    }
}
