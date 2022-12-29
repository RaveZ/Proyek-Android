package com.example.proyek_android

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
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

    private var arCategory = arrayListOf<String>() //arraylist yang menyimpan category

    private var arForum = arrayListOf<Forum>() //arraylist yang ditampilkan

    private val arAllForum = arrayListOf<Forum>() //menyimpan semua forum yang ada di database

    private fun dateValue(date : String): Double {
        //tahun
        var tempData = date.substring(0, 4)
        var hasil : Double = tempData.toDouble()

        //bulan
        tempData = date.substring(5, 7)
        hasil += tempData.toDouble() / 12.0

        //hari
        tempData = date.substring(8, 10)
        hasil += tempData.toDouble() / 360.0

        //jam
        tempData = date.substring(11, 13)
        hasil += tempData.toDouble() / 8640.0

        //menit
        tempData = date.substring(14, 16)
        hasil += tempData.toDouble() / 518400.0

        //detik
        tempData = date.substring(17, 19)
        hasil += tempData.toDouble() / 31104000.0

        return hasil
    }

    private fun sortData(type : Boolean = false){ //membuat arraylist dengan data dan urutan tampilannya, type 0 untuk newest dan 1 untuk trending dengan default 0
        var tempArr = arrayListOf<Forum>()
        tempArr.addAll(arAllForum)
        arForum.clear()
        while (tempArr.size > 0){
            var tempIndex : Int = 0
            for (position in tempArr.indices){
                if (type){
                    if (tempArr.get(position).LikeCount > tempArr.get(tempIndex).LikeCount){
                        tempIndex = position
                    }
                } else{
                    if (dateValue(tempArr.get(position).DateCreated) > dateValue(tempArr.get(tempIndex).DateCreated)){
                        tempIndex = position
                    }
                }
            }
            arForum.add(tempArr.get(tempIndex))
            tempArr.removeAt(tempIndex)
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

        adapterF.setOnItemClickCallback(object : adapterForum.OnItemClickCallback{
            override fun onItemClicked(data: Forum) {
                val intent = Intent(this@Homepage, postDetail::class.java)
                intent.putExtra("data user", data.id.toString())
                startActivity(intent)
            }

        })
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
                }
                db.collection("Categories")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            arCategory.add(document.data.getValue("Category").toString())
                        }
                        val _categoryFilter = findViewById<Spinner>(R.id.categoryFilter)
                        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arCategory)
                        _categoryFilter.adapter = adapter
                        _categoryFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                            }

                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                sortData(_categoryFilter.selectedItemPosition)
                                showData(_rvForum)
                            }
                        }
                        sortData()
                        showData(_rvForum)
                        _btnNewest.setOnClickListener {
                            sortData(false)
                            showData(_rvForum)
                        }
                        _btnTrending.setOnClickListener {
                            sortData(true)
                            showData(_rvForum)
                        }
                    }
            }



//        arAllForum.add(Forum(0, 0,"tita", "hello", 1, "2022/12/23 11:12:05", 0,))
//        arAllForum.add(Forum(1, 0,"hm", "hm", 3, "2022/12/26 20:03:35", 5,))
//        arAllForum.add(Forum(2, 0,"test", "gm", 0, "2022/12/26 20:54:01", 2,))
//        arAllForum.add(Forum(2, 0,"test", "gm", 0, "2022/12/26 20:54:01", 2,))
//        arCategory.add("Sport")
//        arCategory.add("Gaming")
//        arCategory.add("Healthy Life")
//        arCategory.add("Movies")
//        arCategory.add("Art")

        //menampilkan dat
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