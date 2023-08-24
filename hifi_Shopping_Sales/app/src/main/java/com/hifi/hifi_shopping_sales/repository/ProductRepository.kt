package com.hifi.hifi_shopping_sales.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ProductRepository {
    companion object{
        fun getProductAll(sellerIdx:String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("ProductData")
            postDataRef.orderByChild("sellerIdx").equalTo(sellerIdx).get().addOnCompleteListener(callback1)
        }

        fun getProductImgBitmapByFileName(fileName:String, callback1: (Task<Uri>) -> Unit){
            val storage = FirebaseStorage.getInstance()
            val fileRef = storage.reference.child(fileName)

            // 데이터를 가져올 수 있는 경로를 가져온다.
            fileRef.downloadUrl.addOnCompleteListener(callback1)
        }
        // 게시글 정보를 가져온다.
        fun getProductImgListByIdx(productIdx:String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("ProductImgData")
            postDataRef.orderByChild("productIdx").equalTo(productIdx).get().addOnCompleteListener(callback1)
        }
    }
}