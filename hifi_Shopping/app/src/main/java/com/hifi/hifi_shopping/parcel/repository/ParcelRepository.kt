package com.hifi.hifi_shopping.parcel.repository

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ParcelRepository {
    companion object{

        suspend fun getParcelInfoByUserIdx(userIdx: String): DataSnapshot = withContext(Dispatchers.IO) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("OrderData")
            val task = postDataRef.orderByChild("buyerIdx").equalTo(userIdx).get()
            task.await()
        }
        suspend fun getProductInfoByIdx(idx: String): DataSnapshot = withContext(Dispatchers.IO) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("ProductData")
            val task = postDataRef.child(idx).get()
            task.await()
        }
        suspend fun getProductImgByProductIdx(productIdx:String): DataSnapshot = withContext(
            Dispatchers.IO) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("ProductImgData")
            val task = postDataRef.orderByChild("productIdx").equalTo(productIdx).get()
            task.await()
        }

        suspend fun getProductImgByFilename(fileName: String): Uri? = withContext(Dispatchers.IO) {
            val storage = FirebaseStorage.getInstance()
            val fileRef: StorageReference = storage.reference.child("product/$fileName")

            return@withContext try {
                fileRef.downloadUrl.await()
            } catch (e: Exception) {
                null
            }
        }
    }
}