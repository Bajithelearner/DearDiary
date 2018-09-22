package com.example.thevampire.deardiary.deardiary.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.example.thevampire.deardiary.R
import com.example.thevampire.deardiary.deardiary.database.DiaryDataBase
import com.example.thevampire.deardiary.deardiary.database.database.AsyncBackgroundTask

import com.example.thevampire.deardiary.deardiary.database.entity.DairyItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddDiaryActivity : AppCompatActivity() {

    lateinit var mcollRef : CollectionReference
    lateinit var firebase_auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary)
        setSupportActionBar(findViewById(R.id.mytoolbar))
        firebase_auth = FirebaseAuth.getInstance()
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
            val sdf = SimpleDateFormat("dd-MM-yyyy, HH : mm a").format(now.time)
            val dairy  = DairyItem(null,sdf.toUpperCase(),title.text.toString(),body.text.toString())
            insert(dairy)

            Toast.makeText(this,"Inserted",Toast.LENGTH_LONG).show()
            startActivity(Intent(this,FeedActivity::class.java))
            finish()

        }
        return true
    }

   private  fun insert(diary: DairyItem)
    {
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



