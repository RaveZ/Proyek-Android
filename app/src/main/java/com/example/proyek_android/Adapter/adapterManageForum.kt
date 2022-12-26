package com.example.proyek_android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyek_android.DataClass.Forum
import com.example.proyek_android.R

class adapterManageForum(private val listForum : MutableList<Forum>) :
    RecyclerView.Adapter<adapterManageForum.ListViewHolder>(){

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback{
        fun delData(dForum : Forum)
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapterManageForum.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listForum.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    inner class ListViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView){

    }



}