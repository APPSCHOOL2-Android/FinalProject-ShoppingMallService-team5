package com.hifi.hifi_shopping.user.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class ProductRepository {
    companion object{
        fun getProductInfoByIdx(idx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val productDataRef = database.getReference("ProductData")

            productDataRef.orderByChild("idx").equalTo(idx).get()
                .addOnCompleteListener(callback1)

        }
    }
}