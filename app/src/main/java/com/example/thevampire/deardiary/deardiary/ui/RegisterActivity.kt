package com.example.thevampire.deardiary.deardiary.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.thevampire.deardiary.R


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(findViewById(R.id.register_toolbar))

    }
}
