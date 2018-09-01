package com.example.thevampire.deardiary.deardiary.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import com.example.thevampire.deardiary.R
import com.example.thevampire.deardiary.deardiary.utils.FireBaseAuthUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun loginuser(v : View?) : View?
    {
        val username = edit_id.text.toString().trim()
        val password = edit_password.text.toString()
        if(username.isNotEmpty() && password.isNotEmpty() )
        {
            FireBaseAuthUtil(this@MainActivity).signIn_with_Email(v,username,password)

        }
        else
        {
            showMessage(v,"You need to enter username and password")
        }


        return v
    }

    fun forgotuser( v: View?) : View?
    {

       // val intent = Intent(this, FeedActivity::class.java)
        //startActivity(intent)
        return v
    }

    fun registeractivity_start(v : View?) :View?
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        return v
    }

    private fun showMessage(v: View?, msg : String)
    {
        val vv : View = v as View
        Snackbar.make(vv ,msg, Snackbar.LENGTH_SHORT).show()
    }


}
