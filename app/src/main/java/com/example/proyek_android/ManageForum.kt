package com.example.proyek_android

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyek_android.Adapter.adapterManageForum
import com.example.proyek_android.DataClass.Forum
import com.google.firebase.firestore.FirebaseFirestore

class ManageForum : AppCompatActivity() {

    private lateinit var adapterF : adapterManageForum

    lateinit  var db : FirebaseFirestore
    private var arForum : MutableList<Forum> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_forum)


        val _btnBackManageForum = findViewById<ImageView>(R.id.btnBackManageForum)
        var _rvManageForum = findViewById<RecyclerView>(R.id.rvManageForum)
        //database
        db = FirebaseFirestore.getInstance()
        //fetch data
        db.collection("tbForum")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val forum = Forum(
                        document.data.getValue("id").toString().toInt(),
                        document.data.getValue("title").toString(),
                        document.data.getValue("description").toString(),
                        document.data.getValue("category").toString().toInt(),
                        document.data.getValue("dateCreated").toString(),
                        document.data.getValue("likeCount").toString().toInt()
                    )
                    arForum.add(forum)
                }
                adapterF = adapterManageForum(arForum)

                _rvManageForum.layoutManager = LinearLayoutManager(this)
                _rvManageForum.adapter = adapterF

                adapterF.setOnItemClickCallback(object: adapterManageForum.OnItemClickCallback{
                    override fun delData(pos: Int) {
                        AlertDialog.Builder(this@ManageForum)
                            .setTitle("DELETE FORUM")
                            .setMessage("Are you sure?")
                            .setPositiveButton(
                                "DELETE",
                                DialogInterface.OnClickListener{ dialog, which ->
                                    db.collection("tbForum").document("${pos}").delete()
                                    arForum.removeAt(pos)
                                    adapterF = adapterManageForum(arForum)
                                    _rvManageForum.adapter = adapterF
                                }
                            )
                            .setNegativeButton(
                                "CANCEL",
                                DialogInterface.OnClickListener{ dialog, which ->

                                }
                            ).show()
                    }


                })
            }



        _btnBackManageForum.setOnClickListener {
            super.onBackPressed()
        }
    }

        override fun onStart() {
            super.onStart()

        }
}