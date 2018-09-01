package com.example.thevampire.deardiary.deardiary.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.sql.Date
import java.util.*


@Entity(tableName = "dairy")
data class DairyItem(@PrimaryKey(autoGenerate = true) var did: Int?, @ColumnInfo var date: String, @ColumnInfo var title : String, @ColumnInfo var body: String)
{
    constructor() : this(0,"","","")
}