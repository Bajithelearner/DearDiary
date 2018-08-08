package com.example.thevampire.deardiary.deardiary.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room

import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.thevampire.deardiary.deardiary.database.Dao.DairyDao
import com.example.thevampire.deardiary.deardiary.database.entity.DairyItem

@Database(entities = arrayOf(DairyItem::class), version = 1)
abstract class DiaryDataBase : RoomDatabase()
{

    abstract fun getDao() : DairyDao

    companion object {


     var INSTANCE: DiaryDataBase? = null

        fun getInstance(context: Context): DiaryDataBase? {
            if (INSTANCE == null) {
                    synchronized(DiaryDataBase::class){
                        if(INSTANCE ==  null)
                        {
                            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    DiaryDataBase::class.java, "my-data.db")
                                    .build()
                        }
                    }


            }
            return INSTANCE
        }
    }
}
