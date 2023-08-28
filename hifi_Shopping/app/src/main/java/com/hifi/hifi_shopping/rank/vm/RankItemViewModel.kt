package com.hifi.hifi_shopping.rank.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mini01_lbs01.model.ProductDataClass
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.rank.repository.RankItemRepository
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


    fun getProductImg(productIdx:String){
        RankItemRepository.getProductImgSrc(productIdx) {
            for (c1 in it.result.children) {
                val imgSrc = c1.child("imgSrc").value as String
                val default = c1.child("default").value as String

                if (default == "true") {
                    val storage = FirebaseStorage.getInstance()
                    val fileRef = storage.reference.child("product/${imgSrc}")

                    // 데이터를 가져올 수 있는 경로를 가져온다.
                    fileRef.downloadUrl.addOnCompleteListener {
                        thread {
                            // 파일에 접근할 수 있는 경로를 이용해 URL 객체를 생성한다.
                            val url = URL(it.result.toString())
                            // 접속한다.
                            val httpURLConnection = url.openConnection() as HttpURLConnection
                            // 이미지 객체를 생성한다.
                            val bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)
                            productImgBitmap.value = bitmap
                        }
                    }
                }
            }
        }
    }
}

