package com.example.thevampire.deardiary.deardiary.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.example.thevampire.deardiary.R
import com.example.thevampire.deardiary.deardiary.database.DiaryDataBase
import com.example.thevampire.deardiary.deardiary.database.database.AsyncBackgroundTask

import com.example.thevampire.deardiary.deardiary.database.entity.DairyItem
import java.text.SimpleDateFormat
import java.util.*

class AddDiaryActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary)
        setSupportActionBar(findViewById(R.id.mytoolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item?.itemId == R.id.saveitem)
        {

            var title : EditText = findViewById(R.id.edit_title)
            var body : EditText =  findViewById(R.id.edit_data)
            //val date = Date.from()
            //val now = java.util.Date()
            val now = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd-MM-yyyy, HH : MM a").format(now.time)
            val dairy  = DairyItem(null,sdf.toString(),title.text.toString(),body.text.toString())
            insert(dairy)

            Toast.makeText(this,"Inserted",Toast.LENGTH_LONG).show()

        }
        return true
    }

   private  fun insert(diary: DairyItem)
    {
        /*doAsync {

            DiaryDataBase.getInstance(this@AddDiaryActivity)?.getDao()?.add(dairy = diary)
                uiThread {
                //Update the UI thread here
                alert("Row inserted!") {
                    yesButton { toast("Yay !") }
                    noButton { toast(":( !") }
                }.show()
            }
        }*/
        val db = DiaryDataBase.getInstance(this@AddDiaryActivity)
        PopulateAsync.populateAsync(db, diary)

        }

    class PopulateAsync {

        companion object {
            fun populateAsync(db : DiaryDataBase?, item : DairyItem)
            {
                val tsk = AsyncBackgroundTask(db)
                tsk.execute(item)
            }
        }
    }

    }



