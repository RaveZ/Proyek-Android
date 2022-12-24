package com.example.proyek_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class postDetail : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        var btnLike = findViewById<ImageButton>(R.id.btnLike)
        var btnAddComment = findViewById<ImageButton>(R.id.btnAddComment)
        var tvTitle = findViewById<TextView>(R.id.tvTitle)
        var tvIsi = findViewById<TextView>(R.id.tvIsiPost)
        var tvTglPost = findViewById<TextView>(R.id.tvTanggal)
        var tvLike = findViewById<TextView>(R.id.tvJumlahLike)
        db = FirebaseFirestore.getInstance()
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

        }
        btnAddComment.setOnClickListener{

        }
    }
    companion object{
        const val dataUser = "data user"
    }
}