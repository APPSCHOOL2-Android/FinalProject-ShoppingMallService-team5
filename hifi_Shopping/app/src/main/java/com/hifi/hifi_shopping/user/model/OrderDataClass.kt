package com.hifi.hifi_shopping.user.model

data class OrderDataClass(
    val addressIdx: String,
    val buyerIdx: String,
    val couponIdx: String?,
    val date: String,
    val idx: String,
    val price: String,
    val productIdx: String,
    val status: String
)