package com.hifi.hifi_shopping.subscribe.repository

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ReviewBySubscribeRepository {
    companion object{
        suspend fun getProductInfoByIdx(idx: String): DataSnapshot = withContext(Dispatchers.IO) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("ProductData")
            val task = postDataRef.orderByChild("idx").equalTo(idx).get()
            task.await()
        }

        suspend fun getReviewAllByWriterIdx(writerIdx: String): DataSnapshot = withContext(Dispatchers.IO) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("ReviewData")
            val task = postDataRef.orderByChild("writerIdx").equalTo(writerIdx).get()
            task.await()
        }

        suspend fun getProductImgByProductIdx(productIdx:String): DataSnapshot = withContext(Dispatchers.IO) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("ProductImgData")
            val task = postDataRef.orderByChild("productIdx").equalTo(productIdx).get()
            task.await()
        }

        suspend fun getProductImgByFilename(fileName: String): Uri? = withContext(Dispatchers.IO) {
            val storage = FirebaseStorage.getInstance()
            val fileRef: StorageReference = storage.reference.child(fileName)

            return@withContext try {
                fileRef.downloadUrl.await()
            } catch (e: Exception) {
                null
            }
        }
    }
}