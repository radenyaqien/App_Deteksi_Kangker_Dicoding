package com.dicoding.asclepius.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ResultEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val dao: AppDao
}