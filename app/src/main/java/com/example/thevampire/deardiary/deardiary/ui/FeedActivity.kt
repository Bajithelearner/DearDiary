package com.example.thevampire.deardiary.deardiary.ui

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.thevampire.deardiary.deardiary.adapter.DiaryAdapter
import com.example.thevampire.deardiary.R
import com.example.thevampire.deardiary.deardiary.database.DiaryDataBase
import com.example.thevampire.deardiary.deardiary.database.entity.DairyItem
import kotlinx.android.synthetic.main.activity_feed.*
import org.jetbrains.anko.*

class FeedActivity : AppCompatActivity() {

    val animals : ArrayList<String>? = null
    var diary_list : ArrayList<DairyItem> = ArrayList()
    var diaryadapter : DiaryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        setSupportActionBar(findViewById(R.id.feed_toolbar))
        diaryadapter = DiaryAdapter(diary_list, this@FeedActivity)
        feed_recycler_view.layoutManager = LinearLayoutManager(this@FeedActivity)
        feed_recycler_view.adapter = diaryadapter
        getDiary()
        feed_recycler_view

    }

    private fun getDiary()
    {
        val db = DiaryDataBase.getInstance(this@FeedActivity)
        val tsk = FeedAsyncTask(db)
        tsk.execute()
       // val list = DiaryDataBase.getInstance(this@FeedActivity)?.getDao()?.getthis()
        //diary_list = ArrayList(list)
        Toast.makeText(this,diary_list.size.toString(),Toast.LENGTH_LONG).show()

    }

   inner class FeedAsyncTask(val db : DiaryDataBase?) : AsyncTask<Void, Void, List<DairyItem>>()
    {
        override fun doInBackground(vararg params: Void?): List<DairyItem>? {
            val list = db?.getDao()?.getAll()

            return list
        }

        override fun onPostExecute(result: List<DairyItem>?) {
            super.onPostExecute(result)

            diary_list.clear()

            diary_list.addAll(ArrayList(result))
            diaryadapter?.notifyDataSetChanged()

        }
    }
}

