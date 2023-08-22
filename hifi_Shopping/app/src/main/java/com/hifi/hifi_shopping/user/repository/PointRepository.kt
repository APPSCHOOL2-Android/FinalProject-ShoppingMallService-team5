package com.hifi.hifi_shopping.user.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.user.model.PointDataClass
import com.hifi.hifi_shopping.user.model.UserDataClass

class PointRepository {
    companion object {
        fun addPointData(pointData: PointDataClass, callback1: (Task<Void>) -> Unit) {
//        val pointData1 = PointDataClass(userData.idx,200,"2023-08-22","회원 가입")
//        val pointData2 = PointDataClass(userData.idx,-10,"2023-08-23","상품 구입")

            val database = FirebaseDatabase.getInstance()
            val pointDataRef = database.getReference("PointData")

//        pointDataRef.push().setValue(pointData1)
//        pointDataRef.push().setValue(pointData2)

            pointDataRef.push().setValue(pointData).addOnCompleteListener(callback1)
        }

        fun getPointListByUserEmail(userIdx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val pointDataRef = database.getReference("PointData")
            pointDataRef.orderByChild("userIdx").equalTo(userIdx).get()
                .addOnCompleteListener(callback1)

        }

    }
}