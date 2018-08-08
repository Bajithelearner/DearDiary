package com.example.thevampire.deardiary.deardiary.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.thevampire.deardiary.deardiary.ui.DiaryBodyActivity
import com.example.thevampire.deardiary.R
import com.example.thevampire.deardiary.deardiary.database.entity.DairyItem
import kotlinx.android.synthetic.main.diary_title_layout.view.*
import java.util.*

class DiaryAdapter(val diary_list : ArrayList<DairyItem>, val context : Context) : RecyclerView.Adapter<ViewHolder>()
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
        holder.view_parent_item.setOnClickListener(View.OnClickListener {
            Toast.makeText(context,diary_list[position].title,Toast.LENGTH_LONG).show()
            val i = Intent(context, DiaryBodyActivity::class.java)
            i.putExtra("dairy_title_key",title)
            context.startActivity(i)
        }

        )
    }

}

class ViewHolder(view : View) : RecyclerView.ViewHolder(view)
{
    val view_item_holder_title = view.diary_title
    val view_item_holder_date = view.date_textview
    val view_parent_item = view.parentLayout
}