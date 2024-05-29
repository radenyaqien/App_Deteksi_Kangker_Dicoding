package com.dicoding.asclepius.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: ResultEntity)

    @Query("SELECT * from result")
    fun getAllData(): Flow<List<ResultEntity>>

    @Query("SELECT * from result WHERE id = :id")
    fun getOneData(id: Int): Flow<ResultEntity>

}