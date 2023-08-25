package com.example.chiptest

import android.app.Application
import com.example.chiptest.di.networkModule
import com.example.chiptest.di.repoModules
import com.example.chiptest.di.viewModelModules
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            koin.loadModules(
                listOf(
                    viewModelModules, repoModules, networkModule
                )
            )
        }
    }
}