package com.example.proyek_android

import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyek_android.Adapter.adapterManageForum
import com.example.proyek_android.DataClass.Forum
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ManageForum : AppCompatActivity() {

    private lateinit var adapterF : adapterManageForum

    lateinit  var db : FirebaseFirestore
    lateinit var sp : SharedPreferences
    private var arForum : MutableList<Forum> = mutableListOf()
    private lateinit var _rvManageForum: RecyclerView
    private var userId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_forum)


        val _btnBackManageForum = findViewById<ImageView>(R.id.btnBackManageForum)
        _rvManageForum = findViewById(R.id.rvManageForum)
        //database
        db = FirebaseFirestore.getInstance()

        sp = getSharedPreferences("dataAkun", MODE_PRIVATE)
        userId = sp.getInt("spAkun", 0)



        _btnBackManageForum.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun TampilkanData(){
        db.collection("tbForum")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data.getValue("userId").toString().toInt() == userId){
                        val forum = Forum(
                            document.data.getValue("id").toString().toInt(),
                            userId,
                            document.data.getValue("title").toString(),
                            document.data.getValue("description").toString(),
                            document.data.getValue("category").toString().toInt(),
                            document.data.getValue("dateCreated").toString(),
                            document.data.getValue("likeCount").toString().toInt()
                        )
                        arForum.add(forum)
                    }

                }
                adapterF = adapterManageForum(arForum)

                _rvManageForum.layoutManager = LinearLayoutManager(this)
                _rvManageForum.adapter = adapterF

                adapterF.setOnItemClickCallback(object: adapterManageForum.OnItemClickCallback{
                    override fun delData(dForum : Forum) {
                        AlertDialog.Builder(this@ManageForum)
                            .setTitle("DELETE FORUM")
                            .setMessage("Are you sure?")
                            .setPositiveButton(
                                "DELETE",
                                DialogInterface.OnClickListener{ dialog, which ->
                                    Toast.makeText(this@ManageForum, "${dForum.id}", Toast.LENGTH_LONG).show()
                                    val countQuery = db.collection("tbComment")
                                        .document("${dForum.id}")
                                        .collection("Comments").count()
                                    countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            for(i in 0..task.result.count){
                                                db.collection("tbComment").document("${dForum.id}")
                                                    .collection("Comments").document("${i}").delete()
                                            }
                                            db.collection("tbComment").document("${dForum.id}").delete()
                                        }
                                    }
                                    db.collection("tbForum").document("${dForum.id}").delete()
                                    db.collection("tbLikes")
                                        .get()
                                        .addOnSuccessListener { result ->
                                            for (document in result) {
                                                if(document.data.getValue("idForum").toString().equals(dForum.id.toString())){
                                                    db.collection("tbLikes").document("${document.id}").delete()
                                                }
                                            }
                                            }
                                    arForum.clear()
                                    TampilkanData()
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
    }
    override fun onStart() {
        super.onStart()
        arForum.clear()
        TampilkanData()
        Log.d("test", "haha")
    }
}