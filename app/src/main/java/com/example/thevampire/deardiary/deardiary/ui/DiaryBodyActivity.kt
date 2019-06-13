package com.example.thevampire.deardiary.deardiary.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.example.thevampire.deardiary.R

import com.example.thevampire.deardiary.deardiary.database.DiaryDataBase
import com.example.thevampire.deardiary.deardiary.database.entity.DairyItem
import kotlinx.android.synthetic.main.activity_diary_body.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DiaryBodyActivity : AppCompatActivity() {

    var diaryItem : DairyItem? = null
    lateinit var body_edit_text : EditText
    lateinit var updateItem : MenuItem
    var menu : Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_body)
        setSupportActionBar(findViewById(R.id.body_toolbar))

        setUpIntent()
        body_edit_text = diary_body

        body_edit_text.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(diary_body.text.toString() != diaryItem?.body)
                {

                    menu?.findItem(R.id.Updatebtn)?.isVisible = true
                }
                if(diary_body.text.toString() == diaryItem?.body)
                {
                    menu?.findItem(R.id.Updatebtn)?.isVisible = false
                }

            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dairy_body_toolbar,menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId)
        {
            R.id.Updatebtn -> {
                diaryItem?.body = diary_body.text.toString()
                diaryItem?.upload_status = 0

                var rowUpdateResult : Int?
                doAsync {
                   rowUpdateResult =  DiaryDataBase.getInstance(this@DiaryBodyActivity)?.getDao()?.updateBody(diaryItem)
                    uiThread {
                       Toast.makeText(this@DiaryBodyActivity,"Updated",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@DiaryBodyActivity,FeedActivity::class.java))
                        finish()
                    }
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

   private fun setUpIntent()
    {
        val title = intent?.extras?.get("dairy_title_key") as String
        supportActionBar?.title = title
                doAsync {
              diaryItem  = DiaryDataBase.getInstance(this@DiaryBodyActivity)?.getDao()?.getBody(title)
            uiThread { diary_body?.setText(diaryItem?.body.toString())  }
        }


    }


}
