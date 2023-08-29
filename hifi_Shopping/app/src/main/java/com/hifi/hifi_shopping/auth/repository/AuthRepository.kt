package com.hifi.hifi_shopping.auth.repository

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.auth.model.UserDataClass
import java.util.UUID

class AuthRepository(private val context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().getReference("UserData")
    private val storageReference = FirebaseStorage.getInstance().reference

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