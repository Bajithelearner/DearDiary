package com.example.thevampire.deardiary.deardiary.ui

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.constraintlayout.solver.widgets.Snapshot
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.example.thevampire.deardiary.deardiary.adapter.DiaryAdapter
import com.example.thevampire.deardiary.R
import com.example.thevampire.deardiary.deardiary.database.DiaryDataBase
import com.example.thevampire.deardiary.deardiary.database.entity.DairyItem
import com.example.thevampire.deardiary.deardiary.utils.FireBaseAuthUtil
import com.example.thevampire.deardiary.deardiary.utils.showMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_diary_body.*
import kotlinx.android.synthetic.main.activity_feed.*
import org.jetbrains.anko.*
import com.example.thevampire.deardiary.R.layout.toolbar
import com.example.thevampire.deardiary.deardiary.utils.SessionManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_layout.*
import org.w3c.dom.Text


class FeedActivity : AppCompatActivity() {


    var diary_list : ArrayList<DairyItem> = ArrayList()
    var localitems : ArrayList<DairyItem> = ArrayList()
    var diaryadapter : DiaryAdapter? = null
    var items = mutableListOf<DairyItem?>()
    var firebase_auth = FirebaseAuth.getInstance()
    var mdrawlerLayout : DrawerLayout? = null
    var mdrawerToggle : ActionBarDrawerToggle? = null
    var usernametv : TextView? = null
    lateinit var userEmail : String
    lateinit var firebase_auth_state_listener : FirebaseAuth.AuthStateListener
    private lateinit var session : SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        session = SessionManager(this)

        setSupportActionBar(findViewById(R.id.feed_toolbar))
        supportActionBar?.title = "Active Notes"

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }





        mdrawerToggle?.isDrawerIndicatorEnabled = true

        mdrawlerLayout = findViewById(R.id.mdawlerlayout)
        mdrawerToggle =  object : ActionBarDrawerToggle(this,mdrawlerLayout,R.string.draweropen,R.string.drawerclose){
            override fun onDrawerClosed(view: View){
                super.onDrawerClosed(view)
                //toast("Drawer closed")
            }

            override fun onDrawerOpened(drawerView: View){
                super.onDrawerOpened(drawerView)
                //toast("Drawer opened")
            }
        }

        mdrawerToggle?.syncState()

        mdrawlerLayout?.addDrawerListener(mdrawerToggle as ActionBarDrawerToggle)
        var navigation : NavigationView = findViewById(R.id.navigationview)
        val nav_header = navigation.getHeaderView(0)
        usernametv = nav_header.findViewById(R.id.displayname_nav)

        navigation.setNavigationItemSelectedListener {
           it.isChecked = true
            val msg = it.title
            when(it.itemId)
            {
                R.id.aboutitem ->{
                    mdrawlerLayout?.closeDrawer(GravityCompat.START)
                   // startActivity(Intent(this@FeedActivity,AboutActivity::class.java))
                    MaterialDialog(this@FeedActivity).title(text = "About the App")
                            .message(res = R.string.about_app,
                                    lineHeightMultiplier = 1.4f).positiveButton(text = "Okay").show()
                }

                R.id.additem ->
                {
                    mdrawlerLayout?.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this@FeedActivity,AddDiaryActivity::class.java))
                }



                R.id.logoutitem ->{
                    mdrawlerLayout?.closeDrawer(GravityCompat.START)
                    Toast.makeText(this@FeedActivity,"Signing out",Toast.LENGTH_SHORT).show()
                    firebase_auth.signOut()
                    //startActivity(Intent(this@FeedActivity,MainActivity::class.java))
                    finish()
                }
            }

            true
        }
        firebase_auth_state_listener = object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                if(firebase_auth.currentUser != null)
                {

                }
                else
                {
                    startActivity(Intent(this@FeedActivity,MainActivity::class.java))
                    session.deleteUser()
                    finish()
                }
            }

        }

        userEmail = firebase_auth.currentUser?.email.toString()

        if(firebase_auth.currentUser != null)
        {
            val user = firebase_auth.currentUser

            usernametv?.text = user?.displayName
            //showMessage(user?.displayName.toString())
            diaryadapter = DiaryAdapter(diary_list, this@FeedActivity)
            diaryadapter?.setFirestore(FirebaseFirestore.getInstance())
            feed_recycler_view.layoutManager = LinearLayoutManager(this@FeedActivity)
            feed_recycler_view.adapter = diaryadapter
            getDiary()
        }





    }

    private fun getDiary()
    {
        val db = DiaryDataBase.getInstance(this@FeedActivity)
        val tsk = FeedAsyncTask(db)
        tsk.execute(userEmail)
       // val list = DiaryDataBase.getInstance(this@FeedActivity)?.getDao()?.getthis()
        //diary_list = ArrayList(list)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feed_activity_toolbar,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val f = mdrawerToggle?.onOptionsItemSelected(item) as Boolean
        if(f)
        {

            mdrawlerLayout?.openDrawer(GravityCompat.START)
            return true
        }
        else{
            when(item?.itemId)
            {



                R.id.add_new ->{
                    startActivity(Intent(this,AddDiaryActivity::class.java))

                }

                R.id.signout_feed_layoult ->{
                    firebase_auth.signOut()
                }

                R.id.upload ->{
                    //syncToCloud(diary_list)
                    sendToServer()
                }

                R.id.download -> downloadFromServer()


            }
        }


        return super.onOptionsItemSelected(item)
    }

    fun downloadFromServer()
    {
        val firestore = FirebaseFirestore.getInstance()
        val email = firebase_auth.currentUser?.email
        val posts = firestore?.collection("users/$email/posts")
        posts.get().addOnSuccessListener {
            val posts = it.documents
            Log.d("Firebase","Posts Size is "+posts.size)
            val temp_list = ArrayList<DairyItem>()
           temp_list.clear()
            posts.forEach {
                if (it.exists())
                {
                    Log.d("Firebase","Doucument exits")
                    val diaryItem = it.toObject(DairyItem::class.java) as DairyItem
                    diaryItem.upload_status = 1
                    temp_list.add(diaryItem)
                }
                else{
                    Log.d("Firebase","Doucument Does not exits")
                }

            }

            diaryadapter?.setData(temp_list)
            toast("Download Complete!")

            saveToLocalDatabase(temp_list)
        }
    }

    fun saveToLocalDatabase( list : ArrayList<DairyItem>)
    {
        val db = DiaryDataBase.getInstance(this@FeedActivity)
        val newitems = ArrayList<DairyItem>()
        for(listitem in list)
        {
            if(!localitems.contains(listitem))
            {
                Log.d("Firebase","Adding New Item ${listitem.title}")
                listitem.did = null
                newitems.add(listitem)
            }


        }

        doAsync {

            db?.getDao()?.addAll(*newitems.toTypedArray())
            uiThread {
                toast("${newitems.size} Posts are Saved In Database!")
            }
        }

    }

    fun sendToServer()
    {
        val firestore = FirebaseFirestore.getInstance()
        val email = firebase_auth.currentUser?.email
        val batch = firestore.batch()
        val items = ArrayList<DairyItem>()
        localitems.forEach{
            if(it.upload_status ==0)
                items.add(it)
        }

        items.forEach {
           val setop = firestore.collection("users/$email/posts")?.document("${it.title} ${it.did}")
            batch.set(setop,it)
        }

        batch.commit().addOnSuccessListener {
            items.forEach{
                it.upload_status =1

                doAsync {
                    val db = DiaryDataBase.getInstance(this@FeedActivity)
                    db?.getDao()?.updateUploadStatus(it)
                }
            }
            diaryadapter?.notifyDataSetChanged()
            toast("Upload Complete!")
        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        mdrawerToggle?.syncState()
    }

    inner class FeedAsyncTask(val db : DiaryDataBase?) : AsyncTask<String, Void, List<DairyItem>>()
    {
        override fun doInBackground(vararg params: String): List<DairyItem>? {
            val list = db?.getDao()?.getAll(params[0])

            return list
        }

        override fun onPostExecute(result: List<DairyItem>?) {
            super.onPostExecute(result)

            diary_list.clear()
            localitems.clear()
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


}

