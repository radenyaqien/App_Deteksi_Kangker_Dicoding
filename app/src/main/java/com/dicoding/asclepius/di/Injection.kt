package com.dicoding.asclepius.di

import android.content.Context
import androidx.room.Room
import com.dicoding.asclepius.data.local.AppDatabase

object Injection {


    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "history"
        ).build()
    }


}