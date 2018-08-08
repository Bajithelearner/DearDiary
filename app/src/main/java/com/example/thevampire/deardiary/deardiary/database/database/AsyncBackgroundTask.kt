package com.example.thevampire.deardiary.deardiary.database.database

import android.os.AsyncTask
import com.example.thevampire.deardiary.deardiary.database.DiaryDataBase
import com.example.thevampire.deardiary.deardiary.database.entity.DairyItem

class AsyncBackgroundTask(val mdb : DiaryDataBase?) : AsyncTask<DairyItem, Void, Void>() {
    override fun doInBackground(vararg params: DairyItem) : Void? {
        mdb?.getDao()?.add(params[0])
        return null
    }
}