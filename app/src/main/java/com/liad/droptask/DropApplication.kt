package com.liad.droptask

import android.app.Application
import com.liad.droptask.di.appModule
import org.koin.core.context.startKoin


class DropApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            modules(listOf(appModule))
        }
    }

    companion object {
        lateinit var instance: DropApplication
            private set
    }

}