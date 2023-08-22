package com.hifi.hifi_shopping.user.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class UserCouponRepository {
    fun getUserCouponAll(callback1: (Task<DataSnapshot>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val couponDataRef = database.getReference("UserCouponData")
        couponDataRef.orderByChild("validDate").get().addOnCompleteListener(callback1)
    }
}