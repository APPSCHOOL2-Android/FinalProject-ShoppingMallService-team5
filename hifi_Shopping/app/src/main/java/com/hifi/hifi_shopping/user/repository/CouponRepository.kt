package com.hifi.hifi_shopping.user.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.user.model.CouponDataClass
import com.hifi.hifi_shopping.user.model.PointDataClass
import com.hifi.hifi_shopping.user.model.UserCouponDataClass
import java.util.UUID

class CouponRepository {
    companion object {
        fun addCouponData(couponData: CouponDataClass, callback1: (Task<Void>) -> Unit) {
//        val coupon1idx = UUID.randomUUID().toString()
//        val coupon2idx = UUID.randomUUID().toString()
//
//        val couponData1 = CouponDataClass(coupon1idx,"12", "2023-09-25",50,true)
//        val couponData2 = CouponDataClass(coupon2idx,"13", "2023-09-26",35,true)
//
//        val userCouponData1 = UserCouponDataClass(userData.idx,coupon1idx,true)
//        val userCouponData2 = UserCouponDataClass(userData.idx,coupon2idx, false)

            val database = FirebaseDatabase.getInstance()

            val couponDataRef = database.getReference("CouponData")
//        couponDataRef.push().setValue(couponData1)
//        couponDataRef.push().setValue(couponData2)
//
//        val userCouponDataRef = database.getReference("UserCouponData")
//        userCouponDataRef.push().setValue(userCouponData1)
//        userCouponDataRef.push().setValue(userCouponData2)

            couponDataRef.push().setValue(couponData).addOnCompleteListener(callback1)
        }

        fun getCouponAll(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val couponDataRef = database.getReference("CouponData")
            couponDataRef.orderByChild("validDate").get().addOnCompleteListener(callback1)
        }

        fun getCouponInfo(couponIdx:String, callback1 : (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("CouponData")
            postDataRef.orderByChild("couponIdx").equalTo(couponIdx).get().addOnCompleteListener(callback1)
        }
    }
}