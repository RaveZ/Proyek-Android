package com.example.proyek_android

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proyek_android.DataClass.Forum
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class CreateForum : AppCompatActivity() {
    lateinit  var db : FirebaseFirestore

    private var Categories : MutableList<String> = emptyList<String>().toMutableList()
    private val CategoriesLocal = arrayOf("item 1", "item 2", "item 3")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_forum)

        //database
        db = FirebaseFirestore.getInstance()

        //var
        var selectedCategory = CategoriesLocal[0]
        val _btnCreateForum = findViewById<Button>(R.id.btnCreateForum)
        var _tTitle = findViewById<TextInputEditText>(R.id.tInputDescription)
        var _tDescription = findViewById<TextInputEditText>(R.id.tInputTitle)

        var _spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)

        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this@CreateForum,
            android.R.layout.simple_spinner_item, CategoriesLocal
        )

        //implementation
        _spinnerCategory.adapter = spinnerAdapter
        if(_spinnerCategory != null){
            _spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedCategory = CategoriesLocal[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }


        _btnCreateForum.setOnClickListener {
            val newForum = Forum(_tTitle.text.toString(), _tDescription.text.toString(), selectedCategory)
            db.collection("tbForum").add(newForum)


        }


        //debug
        Log.d("Data",_tDescription.text.toString())

    }
}