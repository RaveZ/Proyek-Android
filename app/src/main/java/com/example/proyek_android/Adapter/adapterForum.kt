package com.example.proyek_android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyek_android.DataClass.Forum
import com.example.proyek_android.R

class adapterForum(private val listForum : ArrayList<Forum>) : RecyclerView.Adapter<adapterForum.ListViewHolder>(){

    inner class ListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var _tvTitle : TextView = itemView.findViewById(R.id.HP_tvTitle)
        var _tvDesc : TextView = itemView.findViewById(R.id.HP_tvDesc)
        var _tvDate : TextView = itemView.findViewById(R.id.HP_tvDate)
        var _tvLike : TextView = itemView.findViewById(R.id.HP_tvLike)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.itemforum, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var forum = listForum[position]

        holder._tvTitle.setText(forum.Title)
        holder._tvDesc.setText(forum.Description)
        holder._tvDate.setText(forum.DateCreated)
        holder._tvLike.setText(forum.LikeCount.toString())
    }

    override fun getItemCount(): Int {
        return listForum.size
    }
}
