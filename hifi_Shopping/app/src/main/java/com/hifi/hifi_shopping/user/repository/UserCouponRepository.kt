package com.hifi.hifi_shopping.user.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class UserCouponRepository {
    companion object {
        fun getUserCouponAll(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val couponDataRef = database.getReference("UserCouponData")
            couponDataRef.get().addOnCompleteListener(callback1)
        }

        fun getUserCouponListByUserIdx(userIdx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userCouponDataRef = database.getReference("UserCouponData")
            userCouponDataRef.orderByChild("userIdx").equalTo(userIdx).get()
                .addOnCompleteListener(callback1)

        }
    }
}