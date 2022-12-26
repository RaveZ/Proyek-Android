package com.example.proyek_android.DataClass

import android.os.Parcelable


data class Forum (
    var id : Int,
    var Title : String,
    var Description : String,
    var Category : Int,
    var DateCreated : String,
    var LikeCount : Int
)