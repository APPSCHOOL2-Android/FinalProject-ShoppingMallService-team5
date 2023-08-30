package com.hifi.hifi_shopping.review.repository

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hifi.hifi_shopping.user.model.ReviewDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ReviewProductRepository {
    companion object{
        suspend fun getProductInfoByIdx(idx: String): DataSnapshot = withContext(Dispatchers.IO) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("ProductData")
            val task = postDataRef.orderByChild("idx").equalTo(idx).get()
            task.await()
        }
        suspend fun getProductImgByProductIdx(productIdx:String): DataSnapshot = withContext(Dispatchers.IO) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("ProductImgData")
            val task = postDataRef.orderByChild("productIdx").equalTo(productIdx).get()
            task.await()
        }

        suspend fun getProductImgByFilename(fileName: String): Uri? = withContext(Dispatchers.IO) {
            if(fileName == "sample_img"){
                return@withContext null
            }
            val storage = FirebaseStorage.getInstance()
            val fileRef: StorageReference = storage.reference.child("product/$fileName")

            return@withContext try {
                fileRef.downloadUrl.await()
            } catch (e: Exception) {
                null
            }
        }
        fun uploadImage(uploadUri: Uri, fileName:String){
            val storage = FirebaseStorage.getInstance()
            val imageRef = storage.reference.child(fileName)
            imageRef.putFile(uploadUri)
        }

        fun addReviewInfo(reviewInfo:ReviewDataClass){
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("ReviewData")
            postDataRef.push().setValue(reviewInfo)
        }
    }
}