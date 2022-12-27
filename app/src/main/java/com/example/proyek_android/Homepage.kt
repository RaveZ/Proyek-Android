package com.example.proyek_android

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyek_android.Adapter.adapterForum
import com.example.proyek_android.DataClass.Forum
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Homepage : AppCompatActivity() {
    lateinit var db : FirebaseFirestore
    lateinit var sp : SharedPreferences

    private var arDates = arrayListOf<String>()

    private var arCategory = arrayListOf<String>() //arraylist yang menyimpan category

    private var arForum = arrayListOf<Forum>() //arraylist yang ditampilkan

    private val arAllForum = arrayListOf<Forum>() //menyimpan semua forum yang ada di database

    private fun sortData(type : Boolean = false){ //membuat arraylist dengan data dan urutan tampilannya, type 0 untuk newest dan 1 untuk trending dengan default 0
        var tempArr = arrayListOf<Forum>()
        tempArr.addAll(arAllForum)
        arForum.clear()
        if (type){
            while (tempArr.size > 0){
                var tempIndex : Int = 0
                for (position in tempArr.indices){
                    if (tempArr.get(position).LikeCount > tempArr.get(tempIndex).LikeCount){
                        tempIndex = position
                    }
                }
                arForum.add(tempArr.get(tempIndex))
                tempArr.removeAt(tempIndex)
            }
        } else {
            val dateTimeFormatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
            val result = arDates.sortedByDescending {
                LocalDate.parse(it, dateTimeFormatter)
            }
            var count = 0
            while (tempArr.size>0){
                for (position in tempArr.indices){
                    if (tempArr.get(position).DateCreated.equals(result.get(count))){
                        arForum.add(tempArr.get(position))
                        tempArr.removeAt(position)
                        count++
                        break
                    }
                }
            }
        }
    }

    private fun sortData(category : Int){
        arForum.clear()
        for (data in arAllForum){
            if (data.Category == category){
                arForum.add(data)
            }
        }
    }

    private fun showData(rv : RecyclerView){ //Menampilkan data ke recycler view
        rv.layoutManager = LinearLayoutManager(this)
        val adapterF = adapterForum(arForum)
        rv.adapter = adapterF
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        val _rvForum = findViewById<RecyclerView>(R.id.rvForum)
        val _btnNewest = findViewById<Button>(R.id.HP_btnNewest)
        val _btnTrending = findViewById<Button>(R.id.HP_btnTrending)


        db = FirebaseFirestore.getInstance()
        sp = getSharedPreferences("dataAkun", MODE_PRIVATE)
        val userId = sp.getInt("spAkun", 0)

        //mengambil data forum dari database
        db.collection("tbForum")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data = Forum(document.data.getValue("id").toString().toInt(), document.data.getValue("userId").toString().toInt(), document.data.getValue("title").toString(), document.data.getValue("description").toString(), document.data.getValue("category").toString().toInt(), document.data.getValue("dateCreated").toString(), document.data.getValue("likeCount").toString().toInt())
                    arAllForum.add(data)
                    arDates.add(document.data.getValue("dateCreated").toString())
                }
            }

        db.collection("Categories")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    arCategory.add(document.data.getValue("Category").toString())
                }
            }

//        arAllForum.add(Forum(0, 0,"tita", "hello", 1, "2022/12/23 11:12:05", 0,))
//        arAllForum.add(Forum(1, 0,"hm", "hm", 3, "2022/12/26 20:03:35", 5,))
//        arAllForum.add(Forum(2, 0,"test", "gm", 0, "2022/12/26 20:54:01", 2,))
//        arDates.add("2022/12/23 11:12:05")
//        arDates.add("2022/12/26 20:03:35")
//        arDates.add("2022/12/26 20:54:01")

        //menampilkan data
        sortData(true)
        showData(_rvForum)
        _btnNewest.setOnClickListener {
            sortData(false)
            showData(_rvForum)
        }
        _btnTrending.setOnClickListener {
            sortData(true)
            showData(_rvForum)
        }
        _rvForum.adapter = adapterForum(arForum)

        //Toast.makeText(this@Homepage, "${userId}", Toast.LENGTH_LONG).show()
        val _btnProfile = findViewById<ImageButton>(R.id.btnProfile)
        val _btnCreateForum = findViewById<ImageView>(R.id.btnCreateForumHome)

        _btnCreateForum.setOnClickListener{
            val intent = Intent(this@Homepage, CreateForum::class.java)
            startActivity(intent)
        }

        _btnProfile.setOnClickListener{
            val intent = Intent(this@Homepage, ProfilePage::class.java)
            startActivity(intent)
        }


    }
}