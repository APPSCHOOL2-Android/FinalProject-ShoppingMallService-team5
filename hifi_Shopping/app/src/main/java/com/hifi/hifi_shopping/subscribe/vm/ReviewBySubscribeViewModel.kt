package com.hifi.hifi_shopping.subscribe.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.subscribe.ReviewBySubscribeClass
import com.hifi.hifi_shopping.subscribe.repository.ReviewBySubscribeRepository
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class ReviewBySubscribeViewModel() :ViewModel() {
    val reviewList = MutableLiveData<MutableList<ReviewBySubscribeClass>>()

    init{
        reviewList.value = mutableListOf<ReviewBySubscribeClass>()
    }
    fun getReviewByWriterIdx(writerIdx:String, writerName:String, writerProfile:Bitmap?) = runBlocking {
        var productPrice = ""
        var productName = ""
        var reviewContext = ""
        var likeCnt = ""
        var productImg:Bitmap? = null

        val reviewDeferred = async(Dispatchers.IO) {
            ReviewBySubscribeRepository.getReviewAllByWriterIdx(writerIdx)
        }

        val reviewResult = reviewDeferred.await()
        var productIdx: String? = null
        for (c1 in reviewResult.children) {
            productIdx = c1.child("productIdx").value as String
            reviewContext = c1.child("context").value as String
            likeCnt = c1.child("likeCnt").value as String
            if (productIdx != null) {
                val productDeferred = async(Dispatchers.IO) {
                    ReviewBySubscribeRepository.getProductInfoByIdx(productIdx)
                }

                val productResult = productDeferred.await()
                for (c2 in productResult.children) {
                    productPrice = c2.child("price").value as String
                    productName = c2.child("name").value as String
                }
                val productImgDeferred = async(Dispatchers.IO) {
                    ReviewBySubscribeRepository.getProductImgByProductIdx(productIdx)
                }

                val productImgResult = productImgDeferred.await()

                var fileName = "sample_img"
                for (c3 in productImgResult.children) {
                    val def = c3.child("default").value as String
                    val order = c3.child("omgOrder").value as String
                    if (def == "true" && order == "1") {
                        fileName = c3.child("imgSrc").value as String
                    }
                }

                val productImgBitmapDeferred = async(Dispatchers.IO) {
                    ReviewBySubscribeRepository.getProductImgByFilename(fileName)
                }

                val productImgBitmapResult = productImgBitmapDeferred.await()

                val job1 = launch(Dispatchers.IO) {
                    // 파일에 접근할 수 있는 경로를 이용해 URL 객체를 생성한다.
                    val url = URL(productImgBitmapResult.toString())
                    // 접속한다.
                    val httpURLConnection = url.openConnection() as HttpURLConnection
                    // 이미지 객체를 생성한다.
                    val bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)
                    productImg = bitmap
                }
                job1.join()
            }
            // todo : 이미지가 로딩 되면 Row가 생성되는게 아닌, 이미지는 후처리 하도록 해야 함
            val newReview = ReviewBySubscribeClass(writerIdx, productName, productImg, productPrice,
                writerProfile, writerName, reviewContext, likeCnt)
            reviewList.value?.add(newReview)
        }
    }
}