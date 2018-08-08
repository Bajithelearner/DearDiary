package com.example.thevampire.deardiary.deardiary.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.thevampire.deardiary.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun loginuser(v : View?) : View?
    {
        val intent = Intent(this, AddDiaryActivity::class.java)
        startActivity(intent)
        return v
    }

    fun forgotuser( v: View?) : View?
    {
        Toast.makeText(this,"Loser",Toast.LENGTH_LONG).show()
        val intent = Intent(this, FeedActivity::class.java)
        startActivity(intent)
        return v
    }

    fun registeractivity_start(v : View?) :View?
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        return v
    }
}
