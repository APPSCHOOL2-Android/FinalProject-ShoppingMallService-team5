package com.hifi.hifi_shopping.review.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.review.repository.ReviewProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection
import java.net.URL

class ReviewProductViewModel() : ViewModel() {
    val productName = MutableLiveData<String>()
    val productPrice = MutableLiveData<String>()
    val productImg = MutableLiveData<Bitmap>()

    fun getProductByIdx(productIdx:String) = runBlocking {
        val productDeferred = async(Dispatchers.IO) {
            ReviewProductRepository.getProductInfoByIdx(productIdx)
        }

        val productResult = productDeferred.await()
        for (c2 in productResult.children) {
            productPrice.value = c2.child("price").value as String
            productName.value = c2.child("name").value as String
            break
        }
        val productImgDeferred = async(Dispatchers.IO) {
            ReviewProductRepository.getProductImgByProductIdx(productIdx)
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
            ReviewProductRepository.getProductImgByFilename(fileName)
        }

        val productImgBitmapResult = productImgBitmapDeferred.await()

        launch(Dispatchers.IO) {
            val url = URL(productImgBitmapResult.toString())
            val httpURLConnection = url.openConnection() as HttpURLConnection
            val bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)
            productImg.postValue(bitmap)
        }
    }
}