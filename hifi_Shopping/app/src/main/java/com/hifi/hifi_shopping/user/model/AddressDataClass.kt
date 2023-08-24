package com.hifi.hifi_shopping.user.model

data class AddressDataClass(
    val idx: String,
    val userIdx: String,
    val receiver: String?,
    val receiverPhoneNum: String?,
    val address: String?,
    val context: String?
)