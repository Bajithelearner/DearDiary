package com.example.thevampire.deardiary.deardiary.database.Dao

import androidx.room.*
import com.example.thevampire.deardiary.deardiary.database.entity.DairyItem


@Dao
interface DairyDao{

    @Delete
    fun delete(item : DairyItem) : Int

    @Query("SELECT * FROM DAIRY where author = :email")
    fun getAll(email: String) : List<DairyItem>

    @Insert
    fun add(dairy : DairyItem)

    @Insert
    fun addAll(vararg item : DairyItem)

    @Query("select * from DAIRY where title = :dt")
    fun getBody(dt : String) : DairyItem

    @Query("select * from dairy")
    fun getthis() : List<DairyItem>

    @Update
    fun updateBody(vararg item : DairyItem?) : Int

    @Query("DELETE FROM dairy ")
    fun deleteAll() : Int

    @Update
    fun updateUploadStatus(vararg item : DairyItem?) : Int

    @Insert
    fun insertAll(vararg items : DairyItem)



}