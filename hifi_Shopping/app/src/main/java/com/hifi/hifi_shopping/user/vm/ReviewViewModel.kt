package com.hifi.hifi_shopping.user.vm

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.user.model.PointDataClass
import com.hifi.hifi_shopping.user.model.ReviewDataClass
import com.hifi.hifi_shopping.user.repository.PointRepository
import com.hifi.hifi_shopping.user.repository.ReviewRepository

class ReviewViewModel : ViewModel() {
    var reviewDataList = MutableLiveData<MutableList<ReviewDataClass>>()

    fun getReviewListByUser(userIdx : String){

        val tempList = mutableListOf<ReviewDataClass>()

        ReviewRepository.getReviewAllByUserIdx(userIdx) {
            for (c1 in it.result.children) {
                val idx = c1.child("idx").value as String
                val productIdx = c1.child("productIdx").value as String
                val title = c1.child("title").value as String
                val context = c1.child("context").value as String
                val score = c1.child("score").value as String
                val writerIdx = c1.child("writerIdx").value as String
                val likeCnt = c1.child("likeCnt").value as String
                val date = c1.child("date").value as String
                val imgSrc = c1.child("imgSrc").value as String

                val data = ReviewDataClass(idx, productIdx, title, context, score, writerIdx, likeCnt, date, imgSrc)
                tempList.add(data)
            }
        }
    }
}