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
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")
            userDataRef.push().setValue(userData).addOnCompleteListener(callback1)
        }


        fun getUserInfoByUserIdx(userIdx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")
            userDataRef.orderByChild("idx").equalTo(userIdx).get()
                .addOnCompleteListener(callback1)
        }


        fun getUserInfoByUserEmail(loginUserId: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")

            userDataRef.orderByChild("email").equalTo(loginUserId).get()
                .addOnCompleteListener(callback1)
        }

        fun modifyUserInfo(userDataClass: UserDataClass,callback1: (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")

            userDataRef.orderByChild("idx").equalTo(userDataClass.idx).get().addOnCompleteListener {
                for(a1 in it.result.children){
                    a1.ref.child("profileImg").setValue(userDataClass.profileImg)
                    a1.ref.child("nickname").setValue(userDataClass.nickname)
                    a1.ref.child("verify").setValue(userDataClass.verify)
                    a1.ref.child("phoneNum").setValue(userDataClass.phoneNum)
                    a1.ref.child("pw").setValue(userDataClass.pw).addOnCompleteListener(callback1)
                }
            }
        }
    }
}