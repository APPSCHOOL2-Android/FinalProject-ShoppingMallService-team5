package com.hifi.hifi_shopping.user.model

data class CouponDataClass(
    val idx : String,
    val categoryNum : String,
    val validDate : String,
    val discountPercent : Long,
    val isVerify:Boolean
)
