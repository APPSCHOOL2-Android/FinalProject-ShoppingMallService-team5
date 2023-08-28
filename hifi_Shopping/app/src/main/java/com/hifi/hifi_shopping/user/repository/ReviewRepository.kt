package com.hifi.hifi_shopping.user.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.user.model.PointDataClass
import com.hifi.hifi_shopping.user.model.ReviewDataClass

class ReviewRepository {
    companion object{
        fun addReviewData(reviewData: ReviewDataClass, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val reviewDataRef = database.getReference("ReviewData")

            reviewDataRef.push().setValue(reviewData).addOnCompleteListener(callback1)
        }

        fun getReviewAllByUserIdx(userIdx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val reviewDataRef = database.getReference("ReviewData")

            reviewDataRef.orderByChild("writerIdx").equalTo(userIdx).get()
                .addOnCompleteListener(callback1)

        }
    }
}