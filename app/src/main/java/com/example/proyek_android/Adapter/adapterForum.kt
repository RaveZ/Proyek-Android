package com.example.proyek_android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.proyek_android.DataClass.Forum
import com.example.proyek_android.R
import com.google.firebase.firestore.FirebaseFirestore

class adapterForum(private val listForum : ArrayList<Forum>) : RecyclerView.Adapter<adapterForum.ListViewHolder>(){
    private lateinit var onItemClickCallback : OnItemClickCallback
    lateinit var db : FirebaseFirestore

    interface OnItemClickCallback {
        fun onItemClicked(data : Forum)
    }

    fun setOnItemClickCallback(onItemClickCallback :  OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var _tvTitle : TextView = itemView.findViewById(R.id.HP_tvTitle)
        var _tvDesc : TextView = itemView.findViewById(R.id.HP_tvDesc)
        var _tvDate : TextView = itemView.findViewById(R.id.HP_tvDate)
        var _tvLike : TextView = itemView.findViewById(R.id.HP_tvLike)
        var _tvUser : TextView = itemView.findViewById(R.id.HP_tvUser)
        var _forumLayout : ConstraintLayout = itemView.findViewById(R.id.forumLayout)
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
        db = FirebaseFirestore.getInstance()
        var user : String
        db.collection("tbUserDetail").document("${listForum[position].userId}")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    user = document.data?.getValue("nama").toString()
                    holder._tvUser.setText("${user}")
                }
            }
        if(forum.LikeCount == 0 || forum.LikeCount == 1) {
            holder._tvLike.setText(forum.LikeCount.toString() + " Like")
        } else {
            holder._tvLike.setText(forum.LikeCount.toString() + " Likes")
        }

        holder._forumLayout.setOnClickListener {
            onItemClickCallback.onItemClicked(listForum[position])
        }
    }

    override fun getItemCount(): Int {
        return listForum.size
    }
}
