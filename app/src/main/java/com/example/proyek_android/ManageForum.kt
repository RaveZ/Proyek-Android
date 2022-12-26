package com.example.proyek_android

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class ManageForum : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_forum)

        val _btnBackManageForum = findViewById<ImageView>(R.id.btnBackManageForum)

        _btnBackManageForum.setOnClickListener {
            super.onBackPressed()
        }
    }
}