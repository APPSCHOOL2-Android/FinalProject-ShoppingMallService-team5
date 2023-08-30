package com.hifi.hifi_shopping.review.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hifi.hifi_shopping.review.repository.ReviewProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class ReviewProductViewModel() : ViewModel() {
    val productName = MutableLiveData<String>()
    val productPrice = MutableLiveData<String>()
    val productImg = MutableLiveData<Bitmap?>()

    fun getProductByIdx(productIdx:String) {
        viewModelScope.launch {
            val productInfo = ReviewProductRepository.getProductInfoByIdx(productIdx)
            for (c2 in productInfo.children) {
                productPrice.value = c2.child("price").value as String
                productName.value = c2.child("name").value as String
                break
            }
            val productImgInfo = ReviewProductRepository.getProductImgByProductIdx(productIdx)
            var fileName = "sample_img"
            for (c3 in productImgInfo.children) {
                val def = c3.child("default").value as String
                val order = c3.child("omgOrder").value as String
                if (def == "true" && order == "1") {
                    fileName = c3.child("imgSrc").value as String
                }
            }
            val productImgBitmapInfo = ReviewProductRepository.getProductImgByFilename(fileName)

            var bitmap:Bitmap? = null
            if (productImgBitmapInfo != null) {
                withContext(Dispatchers.IO) {
                    val url = URL(productImgBitmapInfo.toString())
                    val httpURLConnection = url.openConnection() as HttpURLConnection
                    bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)
                }
            }
            productImg.postValue(bitmap)
        }
    }
}