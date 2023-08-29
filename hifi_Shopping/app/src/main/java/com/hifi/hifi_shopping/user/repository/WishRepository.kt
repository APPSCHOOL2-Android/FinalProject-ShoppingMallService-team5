package com.hifi.hifi_shopping.user.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class WishRepository {
    companion object{

        fun getWishAllByUserIdx(userIdx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val wishDataRef = database.getReference("WishData")

            wishDataRef.orderByChild("userIdx").equalTo(userIdx).get().addOnCompleteListener(callback1)

        }
    }
}