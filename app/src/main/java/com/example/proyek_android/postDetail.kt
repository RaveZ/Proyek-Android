package com.example.proyek_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class postDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        var btnLike = findViewById<ImageButton>(R.id.btnLike)
        var btnAddComment = findViewById<ImageButton>(R.id.btnAddComment)
        var tvTitle = findViewById<TextView>(R.id.tTitle)
        var tvIsi = findViewById<TextView>(R.id.tvIsiPost)
        var tvTglPost = findViewById<TextView>(R.id.tvTanggal)

    }
}