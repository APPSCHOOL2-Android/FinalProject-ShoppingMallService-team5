package com.hifi.hifi_shopping.auth.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.auth.model.UserDataClass

class UserRepository {

    companion object {
        // 사용자 정보를 저장한다.
        fun addUserData(userData: UserDataClass, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")
            userDataRef.push().setValue(userData).addOnCompleteListener(callback1)
        }

        // 사용자 idx를 통해 사용자 정보를 가져온다.
        fun getUserInfoByUserIdx(userIdx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")

            userDataRef.orderByChild("idx").equalTo(userIdx).get()
                .addOnCompleteListener(callback1)
        }

        // 사용자 이메일를 통해 사용자 정보를 가져온다.
        fun getUserInfoByUserEmail(loginUserId: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")

            userDataRef.orderByChild("email").equalTo(loginUserId).get()
                .addOnCompleteListener(callback1)
        }


        // 사용자 정보를 수정하는 메서드
        fun modifyUserInfo(userData: UserDataClass, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")
            userDataRef.orderByChild("idx").equalTo(userData.idx).get()
                .addOnCompleteListener {
                    for (a1 in it.result.children) {
                        val userRef = a1.ref

                        userRef.child("pw").setValue(userData.pw)
                        userRef.child("nickname").setValue(userData.nickname)
                        userRef.child("verify").setValue(userData.verify)
                        userRef.child("profileImg").setValue(userData.profileImg)
                            .addOnCompleteListener(callback1)
                    }
                }
        }
    }
}