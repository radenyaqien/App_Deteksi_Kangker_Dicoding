package com.dicoding.asclepius.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("result")
data class ResultEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,
    @ColumnInfo("saved_at")
    val savedAt: Long = System.currentTimeMillis(),
    @ColumnInfo("image_uri")
    val imageUri: String,
    @ColumnInfo("label")
    val label: String,
    @ColumnInfo("score")
    val score: Float,
)