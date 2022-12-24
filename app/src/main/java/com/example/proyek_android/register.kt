package com.example.proyek_android

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.example.proyek_android.DataClass.DataRegist
import java.util.*


class register : AppCompatActivity() {
    lateinit var db: FirebaseFirestore


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
        var emailList = ArrayList<String>()
        var msg = ""
        db = FirebaseFirestore.getInstance()
        db.collection("tbUserDetail")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    emailList.add(document.data.getValue("email").toString())
                }
            }
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
                || etRPass.text.toString() == "" || etRTglLahir.text.toString() == ""
            ) {
                tvWarning.setText("Tidak boleh kosong")
                tvWarning.visibility = View.VISIBLE
            } else {
//                tvWarning.setText("lolos")
//                tvWarning.visibility = View.VISIBLE
                var emailFound = false
                for(i in 0..emailList.lastIndex){
                    if(emailList[i] == etREmail.text.toString()){
                        tvWarning.setText("email sudah terdaftar")
                        tvWarning.visibility = View.VISIBLE
                        emailFound = true
                        break
                    }
                }
                if(emailFound == false) {
                    val countQuery = db.collection("tbUserDetail").count()
                    countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = DataRegist(
                                etRNama.text.toString(),
                                etRNoTelp.text.toString(),
                                etREmail.text.toString(),
                                etRTglLahir.text.toString(),
                                etRPass.text.toString(),
                                etRAlamat.text.toString()
                            )
                            db.collection("tbUserDetail")
                                .document(task.result.count.toString())
                                .set(user)
                        } else {
                            Log.d(ContentValues.TAG, "Count failed: ", task.getException())
                        }
                        val intent = Intent(this@register, MainActivity::class.java).apply {
                        }
                        startActivity(intent)
//                        tvWarning.setText("data masuk")
//                        tvWarning.visibility = View.VISIBLE
                    }
                }

            }

        }

    }
}
