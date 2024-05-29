package com.dicoding.asclepius

import android.app.Application
import com.dicoding.asclepius.data.local.AppDatabase
import com.dicoding.asclepius.di.Injection

class MainApp : Application() {

    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        db = Injection.provideDatabase(this)

    }
}