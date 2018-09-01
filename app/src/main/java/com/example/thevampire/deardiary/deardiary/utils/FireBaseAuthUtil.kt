package com.example.thevampire.deardiary.deardiary.utils

import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.view.View
import com.example.thevampire.deardiary.deardiary.ui.AddDiaryActivity
import com.example.thevampire.deardiary.deardiary.ui.FeedActivity

import com.google.firebase.auth.FirebaseAuth

class FireBaseAuthUtil(context : Context) {
    var firebase_auth = FirebaseAuth.getInstance()
    val ctx = context

        fun signIn_with_Email(v : View?, email : String, password : String)
    {
        var isSuccess = true
        firebase_auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            task ->

            if(task.isSuccessful)
            {

                val intent = Intent(ctx, FeedActivity::class.java)
                ctx.startActivity(intent)
            }
            else
            {
                showMessage(v,"Failed "+{task?.exception?.message}.toString() )
            }
        }



    }
    private fun showMessage(v: View?, msg : String)
    {
        val vv : View = v as View
        Snackbar.make(vv ,msg, Snackbar.LENGTH_SHORT).show()
    }
}


