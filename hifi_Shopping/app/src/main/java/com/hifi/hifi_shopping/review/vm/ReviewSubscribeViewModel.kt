package com.hifi.hifi_shopping.review.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.review.ReviewSubscribeClass
import com.hifi.hifi_shopping.review.repository.ReviewSubscribeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection
import java.net.URL

class ReviewSubscribeViewModel() : ViewModel() {
    val subscribeList = MutableLiveData<MutableList<ReviewSubscribeClass>>()

    fun getSubscribeListByUserIdx(userIdx: String) = runBlocking {
        val temp = mutableListOf<ReviewSubscribeClass>()
        val subscribeListInfo = ReviewSubscribeRepository.getSubscribeListByUserIdx(userIdx)

        for (c1 in subscribeListInfo.children) {
            val idx = c1.child("followerIdx").value as String

            val productResult = ReviewSubscribeRepository.getUserInfoByIdx(idx)

            val nickname = productResult.child("nickname")?.value as String
            val filename = productResult.child("profileImg")?.value as String
            val newReviewSubscribeClass = ReviewSubscribeClass(nickname, filename, null)
            temp.add(newReviewSubscribeClass)
        }
        subscribeList.postValue(temp)
    }
}