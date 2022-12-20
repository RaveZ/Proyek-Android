package com.example.proyek_android
//login page
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnLogin = findViewById<Button>(R.id.btnLogin)
        var etUName = findViewById<EditText>(R.id.etUName)
        var etPassword = findViewById<EditText>(R.id.etPassword)
        var warning = findViewById<TextView>(R.id.warning)
        var unameFound = false
        var passFound = false
        btnLogin.setOnClickListener {
            if(unameFound && passFound){
                warning.visibility = View.GONE
            }else{
                warning.visibility = View.VISIBLE
            }
        }
    }
}