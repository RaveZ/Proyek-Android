package com.example.proyek_android

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class Homepage : AppCompatActivity() {

    lateinit var sp : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        sp = getSharedPreferences("dataAkun", MODE_PRIVATE)
        val userId = sp.getInt("spAkun", 0)

        //Toast.makeText(this@Homepage, "${userId}", Toast.LENGTH_LONG).show()
        val _btnProfile = findViewById<ImageButton>(R.id.btnProfile)
        val _btnCreateForum = findViewById<ImageView>(R.id.btnCreateForumHome)

        _btnCreateForum.setOnClickListener{
            val intent = Intent(this@Homepage, CreateForum::class.java)
            startActivity(intent)
        }

        _btnProfile.setOnClickListener{
            val intent = Intent(this@Homepage, ProfilePage::class.java)
            startActivity(intent)
        }


    }
}