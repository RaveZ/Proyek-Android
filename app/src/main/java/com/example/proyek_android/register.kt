package com.example.proyek_android

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import dataRegist
import java.util.*


class register : AppCompatActivity() {
    lateinit var db : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        var etRNama = findViewById<EditText>(R.id.etRNama)
        var etRPass = findViewById<EditText>(R.id.etRPass)
        var etRNoTelp = findViewById<EditText>(R.id.etRNoTelp)
        val etREmail = findViewById<EditText>(R.id.etREmail)
        var etRTglLahir = findViewById<EditText>(R.id.etRTglLahir)
        val datePicker = findViewById<DatePicker>(R.id.tglLahir)
        var etRAlamat = findViewById<EditText>(R.id.etRPostalAddress)
        var tvWarning = findViewById<TextView>(R.id.tvWarning)
        var btnRegist = findViewById<Button>(R.id.btnRegist)
        var btnSubmitDate = findViewById<Button>(R.id.btnSubmitTgl)
        var msg = ""
        db = FirebaseFirestore.getInstance()
        etRTglLahir.setOnClickListener {
            datePicker.visibility = View.VISIBLE
            btnSubmitDate.visibility = View.VISIBLE
            val today = Calendar.getInstance()
            datePicker.init(
                today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)

            ) { view, year, month, day ->
                val month = month + 1
                msg = "$day/$month/$year"
            }
        }
        btnSubmitDate.setOnClickListener {
            btnSubmitDate.visibility = View.GONE
            datePicker.visibility = View.GONE
            etRTglLahir.setText(msg)
        }
        btnRegist.setOnClickListener {
            if (etRNama.text.toString() == "" || etRAlamat.text.toString() == "" || etREmail.text.toString() == "" || etRNoTelp.text.toString() == ""
                || etRPass.text.toString() == "" || etRTglLahir.text.toString() == "") {
                tvWarning.setText("Tidak boleh kosong")
                tvWarning.visibility = View.VISIBLE
            } else {
//                tvWarning.setText("lolos")
//                tvWarning.visibility = View.VISIBLE
                tvWarning.visibility = View.GONE
                val docRef = db.collection("tbUserDetail").document(etREmail.text.toString())
                val source = Source.CACHE
                docRef.get(source).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        tvWarning.setText("Email sudah terdaftar")
                        tvWarning.visibility = View.VISIBLE
                    }else{
                       val user = dataRegist(
                           etRNama.text.toString(),
                           etRNoTelp.text.toString(),
                           etREmail.text.toString(),
                           etRTglLahir.text.toString(),
                           etRPass.text.toString(),
                           etRAlamat.text.toString()
                       )
                        db.collection("tbUserDetail").document(etREmail.text.toString()).set(user)
                        val intent = Intent(this@register, MainActivity::class.java).apply{
                        }
                        startActivity(intent)
//                        tvWarning.setText("data masuk")
//                        tvWarning.visibility = View.VISIBLE
                    }
                }

            }

        }
    }
    companion object{
        const val data = "data"
    }
}