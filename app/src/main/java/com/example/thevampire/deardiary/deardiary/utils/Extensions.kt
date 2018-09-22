package com.example.thevampire.deardiary.deardiary.utils

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import java.util.*
import java.util.regex.Pattern

fun ClosedRange<Int>.random() =
        Random().nextInt((endInclusive + 1) - start) +  start

fun AppCompatActivity.showMessage(v : View,msg : String)
{
    Snackbar.make(v ,msg, Snackbar.LENGTH_SHORT).show()
}

fun String.isValidEmail() : Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()
