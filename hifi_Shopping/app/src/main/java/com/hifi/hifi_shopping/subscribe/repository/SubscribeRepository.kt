package com.hifi.hifi_shopping.subscribe.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SubscribeRepository {
    companion object{
        // 게시글 정보를 가져온다.
        fun getSubscribeAllByUserIdx(userIdx:String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("SubscribeData")
            postDataRef.orderByChild("userIdx").equalTo(userIdx).get().addOnCompleteListener(callback1)
        }

        fun getProfileByUserIdx(userIdx:String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("UserData")
            postDataRef.orderByChild("idx").equalTo(userIdx).get().addOnCompleteListener(callback1)
        }

        fun getProfileImgByfilename(fileName:String, callback1: (Task<Uri>) -> Unit){
            val storage = FirebaseStorage.getInstance()
            val fileRef = storage.reference.child(fileName)

            // 데이터를 가져올 수 있는 경로를 가져온다.
            fileRef.downloadUrl.addOnCompleteListener(callback1)
        }
    }
}