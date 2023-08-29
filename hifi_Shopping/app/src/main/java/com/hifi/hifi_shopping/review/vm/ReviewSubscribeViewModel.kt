package com.hifi.hifi_shopping.review.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.review.repository.ReviewSubscribeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection
import java.net.URL

class ReviewSubscribeViewModel() : ViewModel() {
    val subscribeList = MutableLiveData<MutableList<ReviewSubscribeClass>>()

    init {
        subscribeList.value = mutableListOf<ReviewSubscribeClass>()
    }
    fun getSubscribeListByUserIdx(userIdx: String) = runBlocking {
        val temp = mutableListOf<ReviewSubscribeClass>()

        val subscribeListDeferred = async(Dispatchers.IO) {
                ReviewSubscribeRepository.getSubscribeListByUserIdx(userIdx)
            }

        val subscribeListResult = subscribeListDeferred.await()

        for (c1 in subscribeListResult.children) {
            val idx = c1.child("followerIdx").value as String

            launch(Dispatchers.IO) {
                val productResult = ReviewSubscribeRepository.getUserInfoByIdx(idx)

                val nickname = productResult.children.firstOrNull()?.child("nickname")?.value as String
                val filename = productResult.children.firstOrNull()?.child("profileImg")?.value as String

                if (!nickname.isNullOrBlank() && !filename.isNullOrBlank()) {
                    val productImgResult = ReviewSubscribeRepository.getUserProfileImgByFilename(filename)

                    var bitmap: Bitmap? = null

                    if (productImgResult != null) {
                        val url = URL(productImgResult.toString())
                        val httpURLConnection = url.openConnection() as HttpURLConnection
                        bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)
                    }

                    val newReviewSubscribeClass = ReviewSubscribeClass(nickname, bitmap)

                    // 새로운 ReviewSubscribeClass를 temp에 추가하는 부분을 동기화하여 스레드 안전하게 처리
                    synchronized(temp) {
                        temp.add(newReviewSubscribeClass)
                    }
                }
            }
        }

        // 이 부분에서 모든 launch가 완료된 후 temp 값을 처리할 수 있음
        subscribeList.postValue(temp)
    }

    data class ReviewSubscribeClass(val nickname:String, val profile:Bitmap?)
}