package com.example.thevampire.deardiary.deardiary.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import com.example.thevampire.deardiary.deardiary.ui.DiaryBodyActivity
import com.example.thevampire.deardiary.R
import com.example.thevampire.deardiary.deardiary.database.DiaryDataBase
import com.example.thevampire.deardiary.deardiary.database.entity.DairyItem
import kotlinx.android.synthetic.main.diary_title_layout.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class DiaryAdapter(var diary_list : ArrayList<DairyItem>, val context : Context) : RecyclerView.Adapter<ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

       return ViewHolder(LayoutInflater.from(context).inflate(R.layout.diary_title_layout, parent, false))
    }

    override fun getItemCount(): Int {

        return diary_list.size
    }

    override fun onBindViewHolder(holder : ViewHolder, position: Int) {

        val title = diary_list[position].title
        holder.view_item_holder_title?.text= diary_list[position].title
        //val now = diary_list[position].date

        //val sdf = SimpleDateFormat("dd-MM-yyyy, HH : MM a").format(now)
        holder.view_item_holder_date?.text = diary_list[position].date
        holder.view_parent_item.setOnClickListener {

            val i = Intent(context, DiaryBodyActivity::class.java)
            i.putExtra("dairy_title_key",title)
            context.startActivity(i)
        }
        holder.view_parent_item.setOnLongClickListener {holdIt ->

            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setMessage("Are you Sure?")
                    .setTitle("Delete")
                    .setPositiveButton("Yes") { dialog, which ->
                        removefromDB(diary_list[position])
                        removefromDataSet(position)
                        Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }.create()
         alertDialog.show()



            true
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
                Toast.makeText(context,i.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }

}

class ViewHolder(view : View) : RecyclerView.ViewHolder(view)
{
    val view_item_holder_title = view.diary_title
    val view_item_holder_date = view.date_textview
    val view_parent_item = view.parentLayout
}