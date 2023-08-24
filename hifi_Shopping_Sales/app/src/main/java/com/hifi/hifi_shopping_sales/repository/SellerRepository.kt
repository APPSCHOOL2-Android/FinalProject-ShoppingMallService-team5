package com.hifi.hifi_shopping_sales.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping_sales.seller.ImgClass

class SellerRepository {
    companion object{
        // 사용자 아이디를 통해 사용자 정보를 가져온다.
        fun getUserInfoByUserId(loginUserId:String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("SellerData")
            // userId가 사용자가 입력한 아이디와 같은 데이터를 가져온다.
            userDataRef.orderByChild("email").equalTo(loginUserId).get().addOnCompleteListener(callback1)
        }
    }
}