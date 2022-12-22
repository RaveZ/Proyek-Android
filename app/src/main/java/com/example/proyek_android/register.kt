package com.example.proyek_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        var etRNama = findViewById<EditText>(R.id.etRNama)
        var etRPass = findViewById<EditText>(R.id.etRPass)
        var etRNoTelp = findViewById<EditText>(R.id.etRNoTelp)
        var etREmail = findViewById<EditText>(R.id.etREmail)
        var etRTglLahir = findViewById<EditText>(R.id.etRTglLahir)
        var etRAlamat = findViewById<EditText>(R.id.etRPostalAddress)
        var tvWarning = findViewById<TextView>(R.id.tvWarning)
        var btnRegist = findViewById<Button>(R.id.btnRegist)

        btnRegist.setOnClickListener {
            if(etRNama == null || etRPass == null || etRAlamat == null || etREmail == null || etRNoTelp == null || etRTglLahir == null){
                tvWarning.setText("Tidak boleh kosong")
                tvWarning.visibility = View.VISIBLE
            }
            else{
                tvWarning.visibility = View.GONE

            }

        }

    }
}