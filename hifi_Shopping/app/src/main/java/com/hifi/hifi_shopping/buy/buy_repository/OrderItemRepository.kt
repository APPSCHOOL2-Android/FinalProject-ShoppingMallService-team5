package com.hifi.hifi_shopping.buy.buy_repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class OrderItemRepository {
    companion object{

        fun getOrderProductData(idx: String, callback1: (Task<DataSnapshot>)-> Unit, callback2: (Task<DataSnapshot>)-> Unit){
            val database = FirebaseDatabase.getInstance()
            val productDataRef = database.getReference("ProductData")
            productDataRef.orderByChild("idx").equalTo(idx).get().addOnCompleteListener (callback1)
                .addOnCompleteListener(callback2)
        }

        fun getProductImgSrc(idx:String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val productDataRef = database.getReference("ProductImgData")
            productDataRef.orderByChild("productIdx").equalTo(idx).get()
                .addOnCompleteListener(callback1)
        }
        fun getProductImg(src: String, callback1: (Task<Uri>) -> Unit){
            val storage = FirebaseStorage.getInstance()
            val fileRef = storage.getReference("product").child(src)
            // 데이터를 가져올 수 있는 경로를 가져온다.
            fileRef.downloadUrl.addOnCompleteListener(callback1)
        }
    }
}