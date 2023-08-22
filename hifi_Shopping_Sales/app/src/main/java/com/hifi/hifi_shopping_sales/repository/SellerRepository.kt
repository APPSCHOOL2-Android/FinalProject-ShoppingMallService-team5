package com.hifi.hifi_shopping_sales.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

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