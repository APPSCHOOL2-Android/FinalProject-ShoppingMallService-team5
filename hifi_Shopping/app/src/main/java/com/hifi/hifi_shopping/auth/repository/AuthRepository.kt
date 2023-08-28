package com.hifi.hifi_shopping.auth.repository

import android.content.Context
import android.service.autofill.UserData
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.auth.model.UserDataClass
import java.util.UUID

class AuthRepository(private val context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().getReference("UserData")
    private val storageReference = FirebaseStorage.getInstance().reference

    companion object{

        fun getUserByAuth(
            callback1: (Task<DataSnapshot>) -> Unit,
        ) {
            val auth = FirebaseAuth.getInstance()
            // 현재 로그인한 사용자 가져오기
            val currentUser: FirebaseUser? = auth.currentUser
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")
            if (currentUser != null) {
                val email = currentUser.email
                userDataRef.orderByChild("email").equalTo(email).get()
                    .addOnCompleteListener(callback1)

            }
        }

        fun registerUserData(email: String, password: String, callback1:(AuthResult) -> Unit) {
            val auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(callback1)
        }
    }



    fun loginUser(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }
    fun registerUser(
        email: String,
        password: String,
        nickname: String,
        imageByteArray: ByteArray,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid ?: ""
                    val phoneNum = ""

                    val profileImgRef = storageReference.child("user/${UUID.randomUUID()}.png")
                    val uploadTask = profileImgRef.putBytes(imageByteArray)

                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        profileImgRef.downloadUrl.addOnCompleteListener { urlTask ->
                            if (urlTask.isSuccessful) {
                                val imageUrl = urlTask.result.toString()

                                val userData = UserDataClass(
                                    idx = userId,
                                    email = email,
                                    pw = password,
                                    nickname = nickname,
                                    verify = "false",
                                    phoneNum = phoneNum,
                                    profileImg = imageUrl
                                )

                                databaseReference.child(userId).setValue(userData)
                                    .addOnSuccessListener {
                                        onSuccess()
                                    }
                                    .addOnFailureListener { exception ->
                                        showErrorMessageDialog("연결에 문제가 발생했습니다.")
                                        onError("연결 오류")
                                    }
                            } else {
                                showErrorMessageDialog("연결에 문제가 발생했습니다.")
                                onError("연결 오류")
                            }
                        }
                    }.addOnFailureListener { exception ->
                        showErrorMessageDialog("연결에 문제가 발생했습니다.")
                        onError("연결 오류")
                    }
                } else {
                    showErrorMessageDialog("연결에 문제가 발생했습니다.")
                    onError("연결 오류")
                }
            }
    }

    private fun showErrorMessageDialog(errorMessage: String) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("오류")
        alertDialogBuilder.setMessage("연결에 문제가 발생했습니다")
        alertDialogBuilder.setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}