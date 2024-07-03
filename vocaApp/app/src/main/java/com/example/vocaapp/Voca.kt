package com.example.vocaapp

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "voca")
data class Voca(
    val text: String,
    val mean: String,
    val type: String,
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
) : Parcelable
