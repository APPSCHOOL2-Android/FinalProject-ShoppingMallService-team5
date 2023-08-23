package com.hifi.hifi_shopping.user.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.user.model.CouponDataClass
import com.hifi.hifi_shopping.user.model.PointDataClass
import com.hifi.hifi_shopping.user.model.UserCouponDataClass
import com.hifi.hifi_shopping.user.model.UserDataClass
import java.util.UUID

class CouponRepository {
    companion object {
        fun addCouponData(couponData: CouponDataClass, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val couponDataRef = database.getReference("CouponData")

            couponDataRef.push().setValue(couponData).addOnCompleteListener(callback1)
        }

        fun getCouponAll(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val couponDataRef = database.getReference("CouponData")
            couponDataRef.orderByChild("validDate").get().addOnCompleteListener(callback1)
        }

        fun getCouponInfo(couponIdx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("CouponData")
            postDataRef.orderByChild("couponIdx").equalTo(couponIdx).get()
                .addOnCompleteListener(callback1)
        }
    }
}