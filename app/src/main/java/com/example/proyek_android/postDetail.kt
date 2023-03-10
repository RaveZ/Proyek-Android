package com.example.proyek_android

import adapterComment
import android.content.ContentValues
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyek_android.DataClass.DataRegist
import com.example.proyek_android.DataClass.comment
import com.example.proyek_android.DataClass.like
import com.example.proyek_android.Helper.DateHelper
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore

class postDetail : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var tanggal : String = DateHelper.getCurrentDate()
    private var comList = ArrayList<comment>()
    private lateinit var recView : RecyclerView
    lateinit var sp : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        var btnLike = findViewById<ImageButton>(R.id.btnLike)
        var btnAddComment = findViewById<ImageButton>(R.id.btnAddComment)
        var tvTitle = findViewById<TextView>(R.id.tvTitle)
        var tvIsi = findViewById<TextView>(R.id.tvIsiPost)
        var tvTglPost = findViewById<TextView>(R.id.tvTanggal)
        var tvLike = findViewById<TextView>(R.id.tvJumlahLike)
        var btnSubmitComment = findViewById<Button>(R.id.btnSubmitComment)
        var inputLayout = findViewById<TextInputLayout>(R.id.inputLayout)
        var inputComment = findViewById<TextInputEditText>(R.id.inputComment)
        var tvUname = findViewById<TextView>(R.id.userName)
        db = FirebaseFirestore.getInstance()
        val idPost = intent.getStringExtra(dataUser)


        recView = findViewById(R.id.recView)
        recView.layoutManager = LinearLayoutManager(this)
        val adapterP = adapterComment(comList)
        recView.adapter = adapterP

        var userName = ""
        sp = getSharedPreferences("dataAkun", MODE_PRIVATE)
        val userId = sp.getInt("spAkun", 0)
        db.collection("tbUserDetail").document("${userId}")
            .get()
            .addOnSuccessListener { result ->
                userName= result.data?.getValue("nama").toString()
            }
        var unamePost = ""
        val uname = db.collection("tbUserDetail").document("${idPost}")
        uname.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    unamePost = document.data?.getValue("nama").toString()
                    tvUname.setText("${unamePost}")
                }
            }
        //perlu dapet id post yg di pilih user buat Post id / 1
        val docRef = db.collection("tbForum").document("${idPost}")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    tvIsi.setText(document.data?.getValue("description").toString())
                    tvTglPost.setText(document.data?.getValue("dateCreated").toString())
                    tvTitle.setText(document.data?.getValue("title").toString())
                    tvLike.setText(document.data?.getValue("likeCount").toString())
                    comList.sortByDescending{ it.id}
                    recView.layoutManager = LinearLayoutManager(this)
                    val adapterP = adapterComment(comList)
                    recView.adapter = adapterP
                    getComment()
                    recView.visibility = View.VISIBLE
                }
            }

        //button
        btnLike.setOnClickListener {

            db.collection("tbLikes")
                .get()
                .addOnSuccessListener { result ->
                    var dbHasCreated = false
                    for (document in result) {
                        if(document.data.getValue("idForum").toString().equals(idPost) ){
                            if(document.data.getValue("idUser").toString().equals(userId.toString())){
//                                Toast.makeText(this@postDetail, "nice", Toast.LENGTH_LONG).show()
                                dbHasCreated = true
                                if(document.data.getValue("hasLiked") == false){
                                    //like
//unlike
                                    var like = tvLike.text.toString().toInt() + 1
                                    if(like >= 0) {
                                        tvLike.setText(like.toString())
                                        db.collection("tbForum")
                                            .document("${idPost}")
                                            .update("likeCount", like)
//                                        db.collection("tbLikes")
//                                            .document(i.toString())
//                                            .update("hasLiked", "false")
                                        val data = like(
                                            true,
                                            idPost!!.toInt(),
                                            userId
                                        )
                                        db.collection("tbLikes")
                                            .document("${document.id}")
                                            .set(data)
                                    }
                                }else{
                                    //unlike
                                    var like = tvLike.text.toString().toInt() - 1
                                    if(like >= 0) {
                                        tvLike.setText(like.toString())
                                        db.collection("tbForum")
                                            .document("${idPost}")
                                            .update("likeCount", like)
//                                        db.collection("tbLikes")
//                                            .document(i.toString())
//                                            .update("hasLiked", "false")
                                        val data = like(
                                            false,
                                            idPost!!.toInt(),
                                            userId
                                        )
                                        db.collection("tbLikes")
                                            .document("${document.id}")
                                            .set(data)
                                    }

                                }
                                break
                            }

                        }

                    }

                    if(dbHasCreated == false){
                        //create db like
                        var like = tvLike.text.toString().toInt() + 1
                        if(like >= 0) {
                            tvLike.setText(like.toString())
                            db.collection("tbForum")
                                .document("${idPost}")
                                .update("likeCount", like)

                            val data = like(
                                true,
                                idPost!!.toInt(),
                                userId
                            )
                            db.collection("tbLikes")
                                .add(data)
                        }
                    }

                }

        }

        btnAddComment.setOnClickListener{
            inputLayout.visibility = View.VISIBLE
            recView.visibility = View.GONE
        }

        btnSubmitComment.setOnClickListener {
            val countQuery = db.collection("tbComment")
                .document("${idPost}")
                .collection("Comments").count()
            countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var commentData = comment(
                        task.result.count.toInt(),
                        userName,
                        inputComment.text.toString(),
                        tanggal
                    )
                    db.collection("tbComment")
                        .document("${idPost}")
                        .collection("Comments")
                        .document("${task.result.count}")
                        .set(commentData)
                    inputLayout.visibility = View.GONE
                    inputComment.setText("")
                    getComment()
                    recView.adapter?.notifyItemChanged(task.result.count.toInt())
                } else {
                    Log.d(ContentValues.TAG, "Count failed: ", task.getException())
                }
            }
            recView.visibility = View.VISIBLE
        }
    }

    private fun getComment(){
        val idPost = intent.getStringExtra(dataUser)
        val cntQuery = db.collection("tbComment").document("${idPost}")
            .collection("Comments").count()
        cntQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (i in 0..task.result.count) {
                    comList.clear()
                    val ref = db.collection("tbComment").document("${idPost}")
                        .collection("Comments").document("${i}")
                    ref.get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                if(document.data?.getValue("id") != null){
                                    val com =
                                        comment(
                                            document.data?.getValue("id").toString().toInt(),
                                            document.data?.getValue("nama").toString(),
                                            document.data?.getValue("isi").toString(),
                                            document.data?.getValue("tglComment").toString()
                                        )
                                    comList.add(com)
                                    comList.sortByDescending{it.id}
//                                    recView.adapter?.notifyItemChanged(i.toInt())
                                    recView.adapter?.notifyDataSetChanged()
                                }
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