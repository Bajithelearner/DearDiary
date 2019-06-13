package com.example.thevampire.deardiary.deardiary.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.checkbox.checkBoxPrompt
import com.afollestad.materialdialogs.checkbox.isCheckPromptChecked
import com.example.thevampire.deardiary.deardiary.ui.DiaryBodyActivity
import com.example.thevampire.deardiary.R
import com.example.thevampire.deardiary.deardiary.database.DiaryDataBase
import com.example.thevampire.deardiary.deardiary.database.entity.DairyItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.diary_title_layout.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList

class DiaryAdapter(var diary_list : ArrayList<DairyItem>, val context : Context) : RecyclerView.Adapter<ViewHolder>()
{

    var firebaseFirestore : FirebaseFirestore? = null

    fun setData(temp_list : ArrayList<DairyItem>){
        Log.d("Firebase","Settings data")
        this.diary_list.clear()
        this.diary_list.addAll(temp_list)
        notifyDataSetChanged()
    }

    fun setFirestore(fire : FirebaseFirestore)
    {
        this.firebaseFirestore = fire
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

       return ViewHolder(LayoutInflater.from(context).inflate(R.layout.diary_title_layout, parent, false))
    }

    override fun getItemCount(): Int {

        return diary_list.size
    }


    override fun onBindViewHolder(holder : ViewHolder, position: Int) {

        val title = diary_list[position].title
        holder.view_item_holder_title?.text= diary_list[position].title
        if(diary_list[position].upload_status == 1)
        {
            holder.upload_status_image_vector.setBackgroundResource(R.drawable.ic_cloud_upload_geen_24dp)

        }
        else
        {

            holder.upload_status_image_vector.setBackgroundResource(R.drawable.ic_cloud_upload_red_24dp)

        }


        holder.view_item_holder_date?.text = diary_list[position].date
        holder.view_parent_item.setOnClickListener {

            val i = Intent(context, DiaryBodyActivity::class.java)
            i.putExtra("dairy_title_key",title)
            context.startActivity(i)
        }
        holder.view_parent_item.setOnLongClickListener {holdIt ->

            val alertDialog = AlertDialog.Builder(context)

            MaterialDialog(context).show {
                title(text = "Delete")
                message(text= "Are you Sure?")
                if(diary_list[position].upload_status == 1)
                {
                    checkBoxPrompt(text = "Delete From Cloud?"){}
                }

                positiveButton(text = "Yes"){
                    val isChecked = it.isCheckPromptChecked()
                    val itemToRemove = diary_list[position]
                    removefromDB(itemToRemove)
                    removefromDataSet(position)


                    if(isChecked)
                    {
                        removeFromFirebase(itemToRemove)
                    }
                    Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show()
                    it.dismiss()
                }
                negativeButton(text = "Cancel"){
                        it.dismiss()
                }

            }
//            alertDialog.setMessage("Are you Sure?")
//                    .setTitle("Delete")
//                    .setPositiveButton("Yes") { dialog, which ->
//                        removefromDB(diary_list[position])
//                        removefromDataSet(position)
//                        Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show()
//                        dialog.dismiss()
//                    }
//                    .setNegativeButton("Cancel") { dialog, which ->
//                        dialog.dismiss()
//                    }.create()
//         alertDialog.show()



            true
        }
    }

    private fun removeFromFirebase(item : DairyItem) {
        val auth = FirebaseAuth.getInstance()
        val email = auth.currentUser?.email
        val post = firebaseFirestore?.collection("users/$email/posts")?.document("${item.title} ${item.did}")
        post?.delete()?.addOnSuccessListener {
            Toast.makeText(context,"Deleted From Firebase",Toast.LENGTH_SHORT).show()
        }
    }

    fun removefromDataSet( i : Int)
    {

        diary_list.removeAt(i)
        notifyItemRemoved(i)
        notifyItemRangeChanged(i,diary_list.size)
    }

    fun removefromDB(item : DairyItem)
    {
        doAsync {
            val i = DiaryDataBase.getInstance(context)?.getDao()?.delete(item)
            uiThread {

            }
        }
    }

}

class ViewHolder(view : View) : RecyclerView.ViewHolder(view)
{
    val view_item_holder_title = view.diary_title
    val view_item_holder_date = view.date_textview
    val view_parent_item = view.parentLayout
    val upload_status_image_vector = view.cloud_upload_status_img
}