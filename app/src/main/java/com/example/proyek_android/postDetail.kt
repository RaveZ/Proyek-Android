package com.example.proyek_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyek_android.DataClass.DataRegist
import com.example.proyek_android.DataClass.comment
import com.example.proyek_android.Helper.DateHelper
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

class postDetail : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var tanggal : String = DateHelper.getCurrentDate()
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
        var recView = findViewById<RecyclerView>(R.id.recView)
        val dataUser = intent.getStringExtra(dataUser)
        db = FirebaseFirestore.getInstance()
        //perlu dapet id post yg di pilih user buat Post id / 1
        val docRef = db.collection("tbForum").document("1")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    tvIsi.setText(document.data?.getValue("description").toString())
                    tvTglPost.setText(document.data?.getValue("dateCreated").toString())
                    tvTitle.setText(document.data?.getValue("title").toString())
                    tvLike.setText(document.data?.getValue("likeCount").toString())
                }
            }
        btnLike.setOnClickListener {
            var like = tvLike.text.toString().toInt() + 1
            tvLike.setText(like.toString())
            db.collection("tbForum")
                .document("1")
                .update("likeCount",like.toString())
        }
        btnAddComment.setOnClickListener{
            inputLayout.visibility = View.VISIBLE
            recView.visibility = View.GONE
        }
        btnSubmitComment.setOnClickListener {
            var commentData = comment(
                dataUser,
                inputComment.text.toString(),
                tanggal
            )
            db.collection("tbComment")
                .document("Post id?")
                .set(commentData)
        }
    }
    companion object{
        const val dataUser = "data user"
    }
}