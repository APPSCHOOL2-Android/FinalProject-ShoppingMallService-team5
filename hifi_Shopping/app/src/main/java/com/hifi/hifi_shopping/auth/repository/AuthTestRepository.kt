package com.hifi.hifi_shopping.auth.repository

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.auth.model.UserDataClass

class AuthTestRepository() {
    companion object {
        fun loginUser(email: String, password: String, context:Context, callback1: (AuthResult) -> Unit) {
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val result = task.result
                    callback1(result)
                }else {
                    val exception = task.exception
                    if (exception is FirebaseAuthException) {
                        // Firebase Authentication 예외 처리
                        when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                Log.d("testaaa","Invalid credentials: ${exception.localizedMessage}")
                                val alertDialog = AlertDialog.Builder(context)
                                    .setTitle("로그인 실패")
                                    .setMessage("이메일 또는 비밀번호가 올바르지 않습니다.")
                                    .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
                                    .create()
                                alertDialog.show()
                            }
                            is FirebaseAuthInvalidUserException -> {
                                val alertDialog = AlertDialog.Builder(context)
                                    .setTitle("로그인 실패")
                                    .setMessage("해당 아이디의 사용자가 존재하지 않습니다.")
                                    .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
                                    .create()
                                alertDialog.show()
                            }
                            else -> {
                                Log.d("testaaa","Authentication failed: ${exception.localizedMessage}")
                            }
                        }
                    } else {
                        Log.d("testaaa","Authentication failed: ${exception?.localizedMessage}")
                    }
                }
            }
        }

        fun registerUser(email: String, password: String, callback1: (AuthResult) -> Unit) {
            val auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(callback1)
        }

        fun addUserInfo(userIdx:String, userClass: UserDataClass, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")
            userDataRef.child(userIdx).setValue(userClass).addOnCompleteListener(callback1)
        }
        // 사용자 아이디를 통해 사용자 정보를 가져온다.
        fun getUserInfoByUserId(loginUserEmail:String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData/")

            // userId가 사용자가 입력한 아이디와 같은 데이터를 가져온다.
            userDataRef.child(loginUserEmail).get().addOnCompleteListener(callback1)
        }
    }
}
