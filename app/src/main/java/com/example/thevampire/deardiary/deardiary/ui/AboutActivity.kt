package com.example.thevampire.deardiary.deardiary.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.thevampire.deardiary.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(findViewById(R.id.about_toolbar))

    }
}
