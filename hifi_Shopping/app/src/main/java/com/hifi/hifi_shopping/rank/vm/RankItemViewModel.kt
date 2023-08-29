package com.hifi.hifi_shopping.rank.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mini01_lbs01.model.ProductDataClass
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.rank.repository.RankItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class RankItemViewModel : ViewModel() {
    var productName = MutableLiveData<ProductDataClass>()
    var productImgBitmap = MutableLiveData<Bitmap>()

    fun getRandomProducts() {
        RankItemRepository.getRandomProductIndices(15) { productIndices ->
            productIndices.forEachIndexed { index, productIdx ->
                getProductName(productIdx)
                getProductImg(productIdx)
            }
        }
    }

    fun getProductName(name: String) {
        RankItemRepository.getProductName(name) {
            for (c1 in it.result.children) {
                val category = c1.child("category").value as String
                val context = c1.child("context").value as String
                val idx = c1.child("idx").value as String
                val name = c1.child("name").value as String
                val pointAmount = c1.child("pointAmount").value as String
                val price = c1.child("price").value as String
                val sellerIdx = c1.child("sellerIdx").value as String

                val data =
                    ProductDataClass(category, context, idx, name, pointAmount, price, sellerIdx)
                productName.value = data
            }
        }
    }


    fun getProductImg(productIdx: String) {
        RankItemRepository.getProductImgSrc(productIdx) {
            for (c1 in it.result.children) {
                val imgSrc = c1.child("imgSrc").value as String
                val default = c1.child("default").value as String

                if (default == "true") {
                    val storage = FirebaseStorage.getInstance()
                    val fileRef = storage.reference.child("product/${imgSrc}")

                    // 데이터를 가져올 수 있는 경로를 가져온다.
                    fileRef.downloadUrl.addOnCompleteListener { task ->
                        val imageUrl = task.result.toString()

                        // 코루틴을 사용하여 UI 스레드에서 이미지 다운로드 및 업데이트
                        viewModelScope.launch {
                            withContext(Dispatchers.IO) {
                                // URL을 이용해 비트맵 다운로드
                                val url = URL(imageUrl)
                                val httpURLConnection = url.openConnection() as HttpURLConnection
                                val bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)

                                // UI 스레드에서 LiveData 값을 변경
                                productImgBitmap.postValue(bitmap)
                            }
                        }
                    }
                }
            }
        }
    }
}

