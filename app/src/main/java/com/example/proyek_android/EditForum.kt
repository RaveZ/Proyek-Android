package com.example.proyek_android

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.proyek_android.DataClass.Forum
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore

class EditForum : AppCompatActivity() {
    lateinit  var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_forum)

        var idForum = 0

        //var
        var Categories = ArrayList<String>()
        val _btnEditForum = findViewById<Button>(R.id.btnEditForum)
        val _btnBack = findViewById<ImageView>(R.id.btnBackEditForum)
        var _tEditTitle = findViewById<TextInputEditText>(R.id.tEditTitle)
        var _tEditDescription = findViewById<TextInputEditText>(R.id.tEditDescription)

        var _spinnerEditCategory = findViewById<Spinner>(R.id.spinnerEditCategory)

        //database
        db = FirebaseFirestore.getInstance()
        //fetch data
        db.collection("tbForum").document("${idForum}")
            .get()
            .addOnSuccessListener { result ->
                if(result != null){
                    _tEditTitle.setText(result.data?.getValue("title").toString())
                    _tEditDescription.setText(result.data?.getValue("description").toString())
                    var currentSelectionCategory = result.data?.getValue("category").toString().toInt()
                    var tanggal = result.data?.getValue("dateCreated").toString()
                    var currentLikes = result.data?.getValue("likeCount").toString().toInt()

                    db.collection("Categories")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                Categories.add(document.data.getValue("Category").toString())
                            }
                            //implementation
                            val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                this@EditForum,
                                android.R.layout.simple_spinner_item, Categories
                            )
                            _spinnerEditCategory.adapter = spinnerAdapter
                            if(_spinnerEditCategory != null){

                                _spinnerEditCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                        currentSelectionCategory = position
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            _spinnerEditCategory.setSelection(currentSelectionCategory)
                        }

                    _btnEditForum.setOnClickListener {
                        if(_tEditTitle.text.isNullOrEmpty() || _tEditDescription.text.isNullOrEmpty()){
                            Toast.makeText(this@EditForum, "Title/Description are Empty!", Toast.LENGTH_LONG).show()
                        }else {
                            val editedForum = Forum(
                                _tEditTitle.text.toString(),
                                _tEditDescription.text.toString(),
                                currentSelectionCategory,
                                tanggal,
                                currentLikes
                            )
                            db.collection("tbForum")
                                .document("${idForum}")
                                .set(editedForum)
                                .addOnCompleteListener { task ->
                                    if(task.isSuccessful){
                                        Toast.makeText(this@EditForum, "Changes Applied", Toast.LENGTH_LONG).show()
                                        //back to last page
                                    }

                                }

                        }
                    }
                }

            }

        _btnBack.setOnClickListener {
            super.onBackPressed()
        }
    }
}