package com.example.thevampire.deardiary.deardiary.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.thevampire.deardiary.R
import com.example.thevampire.deardiary.deardiary.utils.FireBaseAuthUtil
import com.example.thevampire.deardiary.deardiary.utils.SessionManager
import com.example.thevampire.deardiary.deardiary.utils.isValidEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var progress_bar : ProgressBar
    override fun onStart() {
        super.onStart()
        val session = SessionManager(this@MainActivity)
        if(session.isLoggedInUser())
            {
                startActivity(Intent(this,FeedActivity::class.java))
                finish()
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.sign_in_toolbar) as Toolbar
        progress_bar = findViewById(R.id.progress_bar)
        setSupportActionBar(toolbar)
       // toolbar.setTitle("Sign In")
        supportActionBar?.title = "Sign In"

    }

    fun loginuser(v : View) : View
    {
        val username = edit_id.text.toString().trim()
        val password = edit_password.text.trim().toString()
        if(username.isNotEmpty() && password.isNotEmpty() && password.length>=8 && username.isValidEmail() )
        {
            progress_bar.visibility = View.VISIBLE
            signInWithEmail(v,username,password)

        }
        else
        {
            if(username.isEmpty())
                edit_id.error = "Enter Email"
            if(!username.isValidEmail())
                edit_id.error = "Enter Valid Email"
            if(password.isEmpty())
                edit_password.error = "Enter Password"
            if(password.length<8)
                edit_password.error = "Min 8 Characters"

            MainActivity.showMessage(v,"Enter Valid Details")
        }


        return v
    }

    fun forgotuser( v: View) : View
    {
        val firebase_auth = FirebaseAuth.getInstance()
        var error :String? = null

       val email = edit_id.text.trim().toString()

        error =  if(email.length>6 && email.isValidEmail()) null else "Enter Valid Email address to Proceed!"
        if(error==null)
        {

            firebase_auth.sendPasswordResetEmail(email).addOnSuccessListener {
                showMessage(v,"Reset Mail is sent to your Email!")
            }.addOnFailureListener{
                showMessage(v,it.message.toString())
                error = "Something Went Wrong!"
            }
        }

        if(error!=null) showMessage(v,error.toString())
        return v
    }

    fun registeractivity_start(v : View?) :View?
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        return v
    }

    companion object {
        private fun showMessage(v: View, msg : String)
        {
            //val vv  = v as View
            Snackbar.make(v ,msg, Snackbar.LENGTH_SHORT).show()
        }
    }

fun signInWithEmail(v : View, email : String, password : String)
{
    var firebase_auth = FirebaseAuth.getInstance()
    firebase_auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
        task ->
        progress_bar.visibility = View.GONE
        if(task.isSuccessful)
        {


            if(task.result.user.isEmailVerified)
            {
                val intent = Intent(this@MainActivity, FeedActivity::class.java)
                val session = SessionManager(this@MainActivity)
                session.createUserWithEmail(email)
                this@MainActivity.startActivity(intent)
                (this@MainActivity as Activity).finish()

            }
            else
            {
                showMessage(v,"Email Address Not Verified!")
            }


        }
        else
        {
            if(task.exception is FirebaseAuthInvalidCredentialsException)
            {
                showMessage(v,"Invalid Credentials!")
            }

            if(task.exception is FirebaseAuthInvalidUserException)
                showMessage(v,"Couldn't find User, Register First!")


        }

    }
}

}
