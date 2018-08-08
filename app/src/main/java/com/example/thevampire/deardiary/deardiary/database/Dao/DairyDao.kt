package com.example.thevampire.deardiary.deardiary.database.Dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.thevampire.deardiary.deardiary.database.entity.DairyItem


@Dao
interface DairyDao{
    @Query("SELECT * FROM DAIRY")
    fun getAll() : List<DairyItem>

    @Insert
    fun add(dairy : DairyItem)

    @Query("select body from DAIRY where title = :dt")
    fun getBody(dt : String) : String

    @Query("select * from dairy")
    fun getthis() : List<DairyItem>



}