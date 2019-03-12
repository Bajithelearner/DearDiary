package com.example.thevampire.deardiary.deardiary.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context : Context) {

    companion object {
        val IS_LOGGED_IN = "is_user_logged_in"
        val USER_EMAIL_ID = "user_email_id"
    }

    private lateinit var preference : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    init {
        preference = context.getSharedPreferences("session_manager",0)
        editor = preference.edit()
    }
    fun createUserWithEmail(email : String)
    {
        editor.putString(USER_EMAIL_ID,email)
        editor.putBoolean(IS_LOGGED_IN,true)
        editor.apply()
    }

    fun deleteUser() {

        editor.putString(USER_EMAIL_ID,null)
        editor.putBoolean(IS_LOGGED_IN,false)
        editor.apply()

    }

    fun getLoggedInUserEmail() : String?{
        return preference.getString(USER_EMAIL_ID,null)
    }

    fun isLoggedInUser() : Boolean  {return preference.getBoolean(IS_LOGGED_IN,false)}

}