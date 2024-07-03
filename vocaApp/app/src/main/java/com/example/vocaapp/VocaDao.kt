package com.example.vocaapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface VocaDao {
    @Query("SELECT * from voca ORDER BY id DESC")
    fun getAll(): List<Voca>

    @Query("SELECT * from voca ORDER BY id DESC LIMIT 1")
    fun getLatestVoca(): Voca

    @Insert
    fun insert(voca: Voca)

    @Delete
    fun delete(voca: Voca)

    @Update
    fun update(voca: Voca)
}