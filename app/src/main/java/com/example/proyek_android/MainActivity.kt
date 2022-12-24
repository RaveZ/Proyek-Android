package com.example.proyek_android
//login page
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = FirebaseFirestore.getInstance()
        var btnLogin = findViewById<Button>(R.id.btnLogin)
        var btnRegist = findViewById<Button>(R.id.btnRegister)
        var etUName = findViewById<EditText>(R.id.etUName)
        var etEmail = findViewById<EditText>(R.id.etEmail)
        var etPassword = findViewById<EditText>(R.id.etPassword)
        var warning = findViewById<TextView>(R.id.warning)
        var unameFound = false
        var passFound = false
        var emailList = ArrayList<String>()
        db.collection("tbUserDetail")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    emailList.add(document.data.getValue("email").toString())
                }
            }
        btnLogin.setOnClickListener {
            if(etEmail.text.toString() == "" || etPassword.text.toString() == "" || etUName.text.toString() == ""){
                warning.setText("Tidak boleh kosong")
                warning.visibility=View.VISIBLE
            }else{
                var pos = 0
                for(i in 0..emailList.lastIndex){
                    if(emailList[i] == etEmail.text.toString()){
                        pos = i
                        break
                    }
                }
                val docRef = db.collection("tbUserDetail").document(pos.toString())
                val source = Source.CACHE
                docRef.get(source).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
//                        warning.setText("test in")
//                        warning.visibility=View.VISIBLE
                        if(document.data?.getValue("nama").toString() == etUName.text.toString()){
//                            warning.setText("test")
//                            warning.visibility=View.VISIBLE
                            if(etPassword.text.toString() == document.data?.getValue("pass").toString()){
//                                warning.setText("data ditemukan")
//                                warning.visibility=View.VISIBLE
                                val intent = Intent(this@MainActivity, postDetail::class.java).apply {
                                    putExtra(postDetail.dataUser, pos.toString())
                                }
                                startActivity(intent)
                            }else{
                                warning.setText("password salah")
                                warning.visibility=View.VISIBLE
                            }
                        }else{
                            warning.setText("username salah")
                            warning.visibility=View.VISIBLE
                        }
                    }else{
                        warning.setText("Email tidak terdaftar")
                        warning.visibility = View.VISIBLE
                    }
                }
            }
        }
        btnRegist.setOnClickListener {
            val intent = Intent(this@MainActivity, register::class.java).apply{
            }
            startActivity(intent)
        }
    }
    companion object{
        const val data = "data"
    }
}