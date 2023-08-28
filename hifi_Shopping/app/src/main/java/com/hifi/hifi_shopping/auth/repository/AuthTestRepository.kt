package com.hifi.hifi_shopping.auth.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.auth.model.UserDataClass

class AuthTestRepository() {
    companion object {
        fun loginUser(email: String, password: String, callback1: (Task<AuthResult>) -> Unit) {
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(callback1)
        }

        fun registerUser(email: String, password: String, callback1: (AuthResult) -> Unit) {
            val auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(callback1)
        }

        fun addUserInfo(userClass: UserDataClass, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")
            userDataRef.push().setValue(userClass).addOnCompleteListener(callback1)
        }
    }
}