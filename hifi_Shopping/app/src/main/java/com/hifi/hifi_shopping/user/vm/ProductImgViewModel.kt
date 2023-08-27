package com.hifi.hifi_shopping.user.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.user.repository.ProductImgRepository
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class ProductImgViewModel:ViewModel() {
    var productImgBitmap = MutableLiveData<Bitmap>()

    fun getProductImg(productIdx:String){
        ProductImgRepository.getProductImgInfoByProductIdx(productIdx) {
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