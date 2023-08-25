package com.hifi.hifi_shopping.buy.buy_vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.hifi.hifi_shopping.buy.buy_repository.OrderItemRepository
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

data class OrderProduct(var idx: String, var name: String, var price: String, var context: String, var category: String, var pointAmount: String, var sellerIdx: String, var img: Bitmap?)
class OrderItemViewModel: ViewModel() {

    var productMap = MutableLiveData<LinkedHashMap<String, OrderProduct>>()
    val tempHashMap =  LinkedHashMap<String, OrderProduct>()

    fun getOrderProductData(idx: String){


        OrderItemRepository.getOrderProductData(idx, {
            for (i1 in it.result.children) {
                val temp = OrderProduct(
                    i1.child("idx").value as String,
                    i1.child("name").value as String,
                    i1.child("price").value as String,
                    i1.child("context").value as String,
                    i1.child("category").value as String,
                    i1.child("pointAmount").value as String,
                    i1.child("sellerIdx").value as String,
                    null
                )
                tempHashMap[idx] = temp
            }
            productMap.value = tempHashMap
        },{
            OrderItemRepository.getProductImgSrc(idx){
                for(c1 in it.result.children){
                    if(c1.child("default").value as String == "true"){
                        OrderItemRepository.getProductImg(c1.child("imgSrc").value as String){
                            thread {
                                // 파일에 접근할 수 있는 경로를 이용해 URL 객체를 생성한다.
                                val url = URL(it.result.toString())
                                // 접속한다.
                                val httpURLConnection =
                                    url.openConnection() as HttpURLConnection
                                val bitmap =
                                    BitmapFactory.decodeStream(httpURLConnection.inputStream)

                                tempHashMap[idx]?.img = bitmap
                                productMap.postValue(tempHashMap)
                            }
                        }
                    }
                }
            }

        })
    }

}