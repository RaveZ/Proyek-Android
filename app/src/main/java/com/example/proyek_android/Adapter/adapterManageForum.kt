package com.example.proyek_android.Adapter

import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.proyek_android.DataClass.Forum
import com.example.proyek_android.EditForum
import com.example.proyek_android.R
import com.example.proyek_android.postDetail

class adapterManageForum(private val listForum : MutableList<Forum>) :
    RecyclerView.Adapter<adapterManageForum.ListViewHolder>(){

    lateinit var sp : SharedPreferences


    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback{
        fun delData(pos : Int)
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapterManageForum.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manage_forum, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listForum.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var forum = listForum[position]

        holder._tvJudul.setText(forum.Title)
        holder._tvTanggal.setText(forum.DateCreated)
        holder._tvDeskripsi.setText(forum.Description)

        holder._btnEdit.setOnClickListener{
            val intent = Intent(it.context, EditForum::class.java)
            intent.putExtra("idForum", forum.id)
            it.context.startActivity(intent)
        }

        holder._btnDelete.setOnClickListener{
            onItemClickCallback.delData(position)
        }

        holder._forumItem.setOnClickListener {
            val intent = Intent(it.context, postDetail::class.java).apply {
                putExtra(postDetail.dataUser, position.toString())
            }
            it.context.startActivity(intent)
        }
    }

    inner class ListViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView){
        var _btnDelete : ImageButton = itemView.findViewById(R.id.MF_btnDelete)
        var _btnEdit : ImageButton = itemView.findViewById(R.id.MF_btnEdit)
        var _forumItem : ConstraintLayout = itemView.findViewById(R.id.MF_ForumItem)

        var _tvJudul : TextView = itemView.findViewById(R.id.MF_tvJudul)
        var _tvDeskripsi : TextView = itemView.findViewById(R.id.MF_tvDeskripsi)
        var _tvTanggal : TextView = itemView.findViewById(R.id.MF_date)
    }

    fun updateData(list : List<Forum>){
        listForum.clear()
        listForum.addAll(list)
        notifyDataSetChanged()
    }



}