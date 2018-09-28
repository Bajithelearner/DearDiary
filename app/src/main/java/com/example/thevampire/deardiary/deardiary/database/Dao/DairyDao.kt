package com.example.thevampire.deardiary.deardiary.database.Dao

import android.arch.persistence.room.*
import com.example.thevampire.deardiary.deardiary.database.entity.DairyItem


@Dao
interface DairyDao{

    @Delete
    fun delete(item : DairyItem) : Int

    @Query("SELECT * FROM DAIRY")
    fun getAll() : List<DairyItem>

    @Insert
    fun add(dairy : DairyItem)

    @Query("select * from DAIRY where title = :dt")
    fun getBody(dt : String) : DairyItem

    @Query("select * from dairy")
    fun getthis() : List<DairyItem>

    @Update
    fun updateBody(vararg item : DairyItem?) : Int

    @Query("DELETE FROM dairy ")
    fun deleteAll() : Int



    @Insert
    fun insertAll(vararg items : DairyItem)



}