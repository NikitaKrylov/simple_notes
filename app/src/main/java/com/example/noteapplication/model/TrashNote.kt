package com.example.noteapplication.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
//@Entity(tableName = "note_table")
data class TrashNote (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val removalTime: String

    ) : Parcelable