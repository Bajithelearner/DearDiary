package com.example.thevampire.deardiary.deardiary.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.thevampire.deardiary.R
import com.example.thevampire.deardiary.deardiary.database.DiaryDataBase
import kotlinx.android.synthetic.main.activity_diary_body.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DiaryBodyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_body)
        setUpIntent()
    }

    fun setUpIntent()
    {
        val title = intent?.extras?.get("dairy_title_key") as String
        var body : String? = ""
        doAsync {
              body  = DiaryDataBase.getInstance(this@DiaryBodyActivity)?.getDao()?.getBody(title)
            uiThread { diary_body?.setText(body)  }
        }


    }


}
