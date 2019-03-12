package com.example.thevampire.deardiary.deardiary.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.example.thevampire.deardiary.R

class AboutActivity : AppCompatActivity() {

    lateinit var toolbar : android.support.v7.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        toolbar = findViewById(R.id.about_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "About App"

    }
}
