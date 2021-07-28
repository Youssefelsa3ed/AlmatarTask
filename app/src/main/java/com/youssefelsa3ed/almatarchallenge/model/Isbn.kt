package com.youssefelsa3ed.almatarchallenge.model

import android.graphics.Bitmap

data class Isbn(
    val url: String,
    var bitmap: Bitmap?,
    var errorMsg: String? = ""
)