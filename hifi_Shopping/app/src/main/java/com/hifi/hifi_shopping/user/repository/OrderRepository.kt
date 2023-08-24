package com.hifi.hifi_shopping.user.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class OrderRepository {
    companion object{
        fun getOrderAllByUserIdx(userIdx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val orderDataRef = database.getReference("OrderData")

            orderDataRef.orderByChild("userIdx").equalTo(userIdx).get()
                .addOnCompleteListener(callback1)

        }
    }
}