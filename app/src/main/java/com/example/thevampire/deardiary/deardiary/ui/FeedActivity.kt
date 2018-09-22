package com.example.thevampire.deardiary.deardiary.ui

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.solver.widgets.Snapshot
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.thevampire.deardiary.deardiary.adapter.DiaryAdapter
import com.example.thevampire.deardiary.R
import com.example.thevampire.deardiary.deardiary.database.DiaryDataBase
import com.example.thevampire.deardiary.deardiary.database.entity.DairyItem
import com.example.thevampire.deardiary.deardiary.utils.FireBaseAuthUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_diary_body.*
import kotlinx.android.synthetic.main.activity_feed.*
import org.jetbrains.anko.*

class FeedActivity : AppCompatActivity() {


    var diary_list : ArrayList<DairyItem> = ArrayList()
    var localitems : ArrayList<DairyItem> = ArrayList()
    var diaryadapter : DiaryAdapter? = null
    var items = mutableListOf<DairyItem?>()
    var firebase_auth = FirebaseAuth.getInstance()

    lateinit var firebase_auth_state_listener : FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        setSupportActionBar(findViewById(R.id.feed_toolbar))

        firebase_auth_state_listener = object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                if(firebase_auth.currentUser != null)
                {

                }
                else
                {
                    startActivity(Intent(this@FeedActivity,MainActivity::class.java))
                    finish()
                }
            }

        }

        if(firebase_auth.currentUser != null)
        {
            val user = firebase_auth.currentUser
            showMessage(user?.displayName.toString())
            diaryadapter = DiaryAdapter(diary_list, this@FeedActivity)
            feed_recycler_view.layoutManager = LinearLayoutManager(this@FeedActivity)
            feed_recycler_view.adapter = diaryadapter
            getDiary()
        }





    }

    private fun getDiary()
    {
        val db = DiaryDataBase.getInstance(this@FeedActivity)
        val tsk = FeedAsyncTask(db)
        tsk.execute()
       // val list = DiaryDataBase.getInstance(this@FeedActivity)?.getDao()?.getthis()
        //diary_list = ArrayList(list)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feed_activity_toolbar,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId)
        {
            R.id.add_new ->{
                startActivity(Intent(this,AddDiaryActivity::class.java))
                finish()
            }

            R.id.signout_feed_layoult ->{
                firebase_auth.signOut()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    inner class FeedAsyncTask(val db : DiaryDataBase?) : AsyncTask<Void, Void, List<DairyItem>>()
    {
        override fun doInBackground(vararg params: Void?): List<DairyItem>? {
            val list = db?.getDao()?.getAll()

            return list
        }

        override fun onPostExecute(result: List<DairyItem>?) {
            super.onPostExecute(result)

            diary_list.clear()
            localitems.addAll(ArrayList(result))
            diary_list.addAll(ArrayList(result))
            diaryadapter?.notifyDataSetChanged()

        }
    }

    override fun onPause() {
        super.onPause()

        firebase_auth.removeAuthStateListener(firebase_auth_state_listener)
    }

    override fun onResume() {
        super.onResume()

        firebase_auth.addAuthStateListener(firebase_auth_state_listener)
    }

    fun showMessage(msg : String)
    {
        Snackbar.make(this.findViewById(R.id.feed_relative_layout) ,msg, Snackbar.LENGTH_SHORT).show()
        }




   /* fun syncLocalAndCloud(clouditems : ArrayList<DairyItem>, localitems : ArrayList<DairyItem>)
    {
        Log.v("count",clouditems.size.toString() + " "+localitems.size.toString())
        if(clouditems.size == localitems.size) { toast("Nothing to update "); return  }// no problem here

        if(clouditems.size < localitems.size)
        {
            for( item in localitems)
            {
                if(!clouditems.contains(item))
                {
                    clouditems.add(item)
                }
            }
            storeToCloudDB(clouditems)
        }
        if(clouditems.size > localitems.size)
        {
            for(item in clouditems)
            {
                if(!localitems.contains(item))
                {
                    localitems.add(item)

                }
            }
            storeToLocalDB(localitems)
        }
    }

    fun storeToLocalDB(clouditems : ArrayList<DairyItem>)
    {
        var rows : Int?
        doAsync {
          rows  = DiaryDataBase.getInstance(this@FeedActivity)?.getDao()?.deleteAll()
            for (clouditem in clouditems) {
                DiaryDataBase.getInstance(this@FeedActivity)?.getDao()?.insertAll(clouditem)
            }
           uiThread {  Log.v("Rows","deleted rows are $rows")
           toast("Deleted the Messages and calling getdiary")

               getDiary()

           }



        }
    }
    fun storeToCloudDB(clouditems : ArrayList<DairyItem>)
    {

        posts_collection.get().addOnCompleteListener {
            if(it.isSuccessful)
            {

                it.result.documents.forEach {
                    firebatch1.delete(it.reference)
                }

            }
        }

        for(item in clouditems)
        {
            var mdoc = posts_collection.document()
            firebatch2.set(mdoc,item)

        }

        firebatch1.commit().addOnCompleteListener {
            if(it.isSuccessful)
            {
                firebatch2.commit()
            }
        }
    }*/
}

