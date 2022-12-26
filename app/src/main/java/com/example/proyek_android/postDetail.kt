package com.example.proyek_android

import adapterComment
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyek_android.DataClass.DataRegist
import com.example.proyek_android.DataClass.comment
import com.example.proyek_android.Helper.DateHelper
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore

class postDetail : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    private lateinit var dbRef: DatabaseReference
    var tanggal : String = DateHelper.getCurrentDate()
    private var comList = ArrayList<comment>()
    private lateinit var recView : RecyclerView
    private var loading = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        var btnLike = findViewById<ImageButton>(R.id.btnLike)
        var btnAddComment = findViewById<ImageButton>(R.id.btnAddComment)
        var tvTitle = findViewById<TextView>(R.id.tvTitle)
        var tvIsi = findViewById<TextView>(R.id.tvIsiPost)
        var tvTglPost = findViewById<TextView>(R.id.tvTanggal)
        var tvLike = findViewById<TextView>(R.id.tvJumlahLike)
        var tvWarningP = findViewById<TextView>(R.id.tvWarningL)
        var btnSubmitComment = findViewById<Button>(R.id.btnSubmitComment)
        var inputLayout = findViewById<TextInputLayout>(R.id.inputLayout)
        var inputComment = findViewById<TextInputEditText>(R.id.inputComment)
        var btnShowComment = findViewById<ImageButton>(R.id.btnShowComment)
        val user = intent.getStringExtra(dataUser)
        db = FirebaseFirestore.getInstance()

        recView = findViewById(R.id.recView)
        recView.layoutManager = LinearLayoutManager(this)
        val adapterP = adapterComment(comList)
        recView.adapter = adapterP

        //perlu dapet id post yg di pilih user buat Post id / 1
        val docRef = db.collection("tbForum").document("1")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    tvIsi.setText(document.data?.getValue("description").toString())
                    tvTglPost.setText(document.data?.getValue("dateCreated").toString())
                    tvTitle.setText(document.data?.getValue("title").toString())
                    tvLike.setText(document.data?.getValue("likeCount").toString())
                    recView.adapter = adapterP
                    getComment()
                }
            }

        //button
        btnLike.setOnClickListener {
            val cQuery = db.collection("tbLikes").count()
            var jmlh = 0
            cQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    jmlh = task.result.count.toString().toInt()
                }
            }
            for(i in 0..jmlh){
                val docRef = db.collection("tbLikes").document(i.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            if(document.data?.getValue("idForum").toString() == "0" && document.data?.getValue("idUser").toString() == "0"){
                                if(document.data?.getValue("hasLiked").toString() == "false"){
                                    var like = tvLike.text.toString().toInt() + 1
                                    tvLike.setText(like.toString())
                                    db.collection("tbForum")
                                        .document("1")
                                        .update("likeCount",like.toString())
                                    db.collection("tbLikes")
                                        .document(i.toString())
                                        .update("hasLiked","true")
                                }else{
                                    var like = tvLike.text.toString().toInt() - 1
                                    tvLike.setText(like.toString())
                                    db.collection("tbForum")
                                        .document("1")
                                        .update("likeCount",like.toString())
                                    db.collection("tbLikes")
                                        .document(i.toString())
                                        .update("hasLiked","false")
                                }
                            }
                        }
                    }
            }
        }

        btnShowComment.setOnClickListener {
            recView.adapter!!.notifyDataSetChanged()
            recView.visibility = View.VISIBLE
            btnAddComment.visibility = View.VISIBLE
        }

        btnAddComment.setOnClickListener{
            inputLayout.visibility = View.VISIBLE
            recView.visibility = View.GONE
        }

        btnSubmitComment.setOnClickListener {
            var commentData = comment(
                user,
                inputComment.text.toString(),
                tanggal
            )
            val countQuery = db.collection("tbComment")
                .document("Post id?")
                .collection("Comments").count()
            countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    db.collection("tbComment")
                        .document("Post id?")
                        .collection("Comments")
                        .document("${task.result.count}")
                        .set(commentData)
                    inputLayout.visibility = View.GONE
                    inputComment.setText("")
                    getComment()
                } else {
                    Log.d(ContentValues.TAG, "Count failed: ", task.getException())
                }
            }
            recView.adapter!!.notifyDataSetChanged()
            recView.visibility = View.VISIBLE
        }
    }

    private fun getComment(){
        val cntQuery = db.collection("tbComment").document("Post id?")
            .collection("Comments").count()
        cntQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                comList.clear()
                for (i in 0..task.result.count) {
                    val ref = db.collection("tbComment").document("Post id?")
                        .collection("Comments").document("${i}")
                    ref.get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
//                                if(document.data?.getValue("nama") != null ||document.data?.getValue("isi")!=null ){
                                    val com =
                                        comment(
                                            document.data?.getValue("nama").toString(),
                                            document.data?.getValue("isi").toString(),
                                            document.data?.getValue("tglComment").toString()
                                        )
                                    comList.add(com)
//                                }
                            }
                        }
                }
            }
        }
//        return false
    }

    companion object{
        const val dataUser = "data user"
    }
}