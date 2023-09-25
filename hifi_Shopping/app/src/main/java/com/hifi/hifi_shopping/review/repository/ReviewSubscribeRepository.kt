package com.hifi.hifi_shopping.review.repository

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ReviewSubscribeRepository {
    companion object{
        suspend fun getSubscribeListByUserIdx(userIdx: String): DataSnapshot = withContext(Dispatchers.IO) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("SubscribeData")
            val task = postDataRef.orderByChild("userIdx").equalTo(userIdx).get()
            task.await()
        }
        suspend fun getUserInfoByIdx(idx:String): DataSnapshot = withContext(
            Dispatchers.IO) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("UserData")
            val task = postDataRef.child(idx).get()
            task.await()
        }

        suspend fun getUserProfileImgByFilename(fileName: String): Uri? = withContext(Dispatchers.IO) {
            val storage = FirebaseStorage.getInstance()
            val fileRef: StorageReference = storage.reference.child("user/$fileName")

            return@withContext try {
                fileRef.downloadUrl.await()
            } catch (e: Exception) {
                null
            }
        }
    }
}