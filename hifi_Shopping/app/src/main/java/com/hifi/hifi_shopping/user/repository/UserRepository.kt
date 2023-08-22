package com.hifi.hifi_shopping.user.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.user.model.UserDataClass
import java.util.UUID

class UserRepository {
    companion object {
        fun addUserData(userData: UserDataClass, callback1: (Task<Void>) -> Unit) {
//        val uuid = UUID.randomUUID()
//        val idx = uuid.toString()

//        val userData = UserDataClass(idx,"ohsso98@naver.com", "0618","김대박",false,"01000000000","user_sample.jpg")
//        val userData = UserDataClass(idx,"alohalo98@naver.com", "1222","김용",false,"01011111111","user_sample.jpg")

            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")
            userDataRef.push().setValue(userData).addOnCompleteListener(callback1)
        }


        fun getUserInfoByUserIdx(userIdx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")

            userDataRef.orderByChild("userIdx").equalTo(userIdx).get()
                .addOnCompleteListener(callback1)

        }

        // 사용자 아이디를 통해 사용자 정보를 가져온다.
        fun getUserInfoByUserId(loginUserId: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")

            // userId가 사용자가 입력한 아이디와 같은 데이터를 가져온다.
            userDataRef.orderByChild("email").equalTo(loginUserId).get()
                .addOnCompleteListener(callback1)
        }
    }
}