package com.example.proyek_android

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proyek_android.DataClass.Forum
import com.example.proyek_android.Helper.DateHelper.getCurrentDate
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore

class CreateForum : AppCompatActivity() {
    lateinit  var db : FirebaseFirestore


    var tanggal : String = getCurrentDate()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_forum)

        //var
        var Categories = ArrayList<String>()
        val _btnCreateForum = findViewById<Button>(R.id.btnCreateForum)
        var _tTitle = findViewById<TextInputEditText>(R.id.tTitle)
        var _tDescription = findViewById<TextInputEditText>(R.id.tDescription)

        var _spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)



        //database
            db = FirebaseFirestore.getInstance()
        //fetch data
        db.collection("Categories")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Categories.add(document.data.getValue("Category").toString())
                }

                //implementation
                var selectedCategory = 0
                val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this@CreateForum,
                    android.R.layout.simple_spinner_item, Categories
                )
                _spinnerCategory.adapter = spinnerAdapter
                if(_spinnerCategory != null){

                    _spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            selectedCategory = position
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                    }
                }


                _btnCreateForum.setOnClickListener {
                    if(_tTitle.text.isNullOrEmpty() || _tDescription.text.isNullOrEmpty()){
                        Toast.makeText(this@CreateForum, "Title/Description still Empty!", Toast.LENGTH_LONG).show()
                    }else {
                        val countQuery = db.collection("tbForum").count()
                        countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val newForum = Forum(
                                    _tTitle.text.toString(),
                                    _tDescription.text.toString(),
                                    selectedCategory,
                                    tanggal,
                                    0
                                )
                                db.collection("tbForum")
                                    .document(task.result.count.toString())
                                    .set(newForum)
                            } else {
                                Log.d(TAG, "Count failed: ", task.getException())
                            }
                        }
                    }
            }









        }


        //debug
        Log.d("Data",Categories.count().toString())

    }
}