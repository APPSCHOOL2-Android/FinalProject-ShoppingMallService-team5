package com.hifi.hifi_shopping.user.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.user.model.PointDataClass
import com.hifi.hifi_shopping.user.model.SubscribeDataClass

class SubscribeRepository {
    companion object{
        fun addSubscribeData(subscribeData: SubscribeDataClass, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val pointDataRef = database.getReference("SubscribeData")

            pointDataRef.push().setValue(subscribeData).addOnCompleteListener(callback1)
        }

        fun getSubscribeListByUserIdx(userIdx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val subscribeDataRef = database.getReference("SubscribeData")
            subscribeDataRef.orderByChild("userIdx").equalTo(userIdx).get()
                .addOnCompleteListener(callback1)

        }

        fun getSubscribeListByFollowerIdx(followerIdx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val subscribeDataRef = database.getReference("SubscribeData")
            subscribeDataRef.orderByChild("userIdx").equalTo(followerIdx).get()
                .addOnCompleteListener(callback1)

        }
    }
}