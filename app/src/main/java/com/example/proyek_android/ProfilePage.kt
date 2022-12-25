package com.example.proyek_android

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.firestore.FirebaseFirestore

class ProfilePage : AppCompatActivity() {
    lateinit  var db : FirebaseFirestore

    lateinit var sp : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        sp = getSharedPreferences("dataAkun", MODE_PRIVATE)
        val userId = sp.getInt("spAkun", 0)

        //database
        db = FirebaseFirestore.getInstance()

        //var
        val _btnLogout = findViewById<ConstraintLayout>(R.id.btnLogout)
        val _btnBack = findViewById<ImageView>(R.id.btnBackProfile)
        val _btnCreatedForum = findViewById<ConstraintLayout>(R.id.btnProfileCreatedForum)
        var _tName = findViewById<EditText>(R.id.tProfileName)
        var _tEmail = findViewById<EditText>(R.id.tProfileEmail)
        var _tNumber = findViewById<EditText>(R.id.tProfileNumber)
        var _tBirthDate = findViewById<EditText>(R.id.tProfileBirthDate)

        db.collection("tbUserDetail").document("${userId}")
            .get()
            .addOnSuccessListener { result ->
                _tName.setText(result.data?.getValue("nama").toString())
                _tEmail.setText(result.data?.getValue("email").toString())
                _tNumber.setText(result.data?.getValue("noTelp").toString())
                _tBirthDate.setText(result.data?.getValue("tglLahir").toString())
            }


        _btnLogout.setOnClickListener{
            AlertDialog.Builder(this@ProfilePage)
                .setTitle("Log out from ForumOnline?")
                .setPositiveButton(
                    "Log out",
                    DialogInterface.OnClickListener{ dialog, which ->
                        val intent = Intent(this@ProfilePage, MainActivity::class.java)
                        startActivity(intent)
                    }
                )
                .setNegativeButton(
                    "Cancel",
                    DialogInterface.OnClickListener{ dialog, which ->
                    }
                ).show()

        }

        _btnCreatedForum.setOnClickListener{

        }
        _btnBack.setOnClickListener{
            super.onBackPressed()
        }


    }
}