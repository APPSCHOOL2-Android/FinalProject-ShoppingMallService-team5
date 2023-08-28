package com.hifi.hifi_shopping.rank.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RankItemRepository {
    companion object{

        // Firebase에 접근하여 랜덤한 제품 인덱스들을 가져오는 메서드 (임시로 랜덤 구현)
        fun getRandomProductIndices(count: Int, callback: (List<String>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val productDataRef = database.getReference("ProductData")
            productDataRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val indices = mutableListOf<String>()
                    val dataSnapshot = task.result
                    dataSnapshot.children.forEach { child ->
                        val idx = child.child("idx").value as String
                        indices.add(idx)
                    }
                    indices.shuffle() // 랜덤으로 섞기
                    val randomIndices = indices.take(count)
                    callback(randomIndices)
                }
            }
        }

        // 제품 이름을 가져옴
        fun getProductName(name: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val productDataRef = database.getReference("ProductData")
            productDataRef.child(name).child("name").get()
                .addOnCompleteListener(callback1)
        }

        fun getProductImgSrc(idx:String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val productDataRef = database.getReference("ProductImgData")
            productDataRef.orderByChild("productIdx").equalTo(idx).get()
                .addOnCompleteListener(callback1)
        }
        // 경로를 통해 제품 이미지 가져옴
        fun getProductImg(src: String, callback1: (Task<Uri>) -> Unit){
            val storage = FirebaseStorage.getInstance()
            val fileRef = storage.getReference("product").child(src)
            fileRef.downloadUrl.addOnCompleteListener(callback1)
        }
    }
}