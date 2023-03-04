package com.example.noteapplication.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
@Entity(tableName = "note_table")
data class Note(

    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val text:String,
    val backgroundColorId:Int,
    val creationDate:Date,
    var isFavourite:  Boolean = false,
    var inTrash: Int = 0,
    val deletingDateTime:Date? = null

): Parcelable