package dev.m13d.somenet.di

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
