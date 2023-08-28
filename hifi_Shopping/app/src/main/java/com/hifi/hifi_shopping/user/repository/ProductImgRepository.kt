package com.hifi.hifi_shopping.user.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ProductImgRepository {
    companion object{
        fun getProductImgInfoByProductIdx(productIdx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val productImgDataRef = database.getReference("ProductImgData")
            productImgDataRef.orderByChild("productIdx").equalTo(productIdx).get()
                .addOnCompleteListener(callback1)

        }

        fun getProductImg(fileName:String, callback1: (Task<Uri>) -> Unit){
            val storage = FirebaseStorage.getInstance()
            val fileRef = storage.reference.child(fileName)

            // 데이터를 가져올 수 있는 경로를 가져온다.
            fileRef.downloadUrl.addOnCompleteListener(callback1)
        }
    }
}