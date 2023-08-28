package com.hifi.hifi_shopping.user.model

data class ReviewDataClass(
    val idx : String,
    val productIdx : String,
    val title : String,
    val context : String,
    val score : String,
    val writerIdx : String,
    val likeCnt : String,
    val date : String,
    val imgSrc : String
)
