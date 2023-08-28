package com.hifi.hifi_shopping.buy.buy_vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.hifi.hifi_shopping.buy.buy_repository.OrderItemRepository
import com.hifi.hifi_shopping.buy.buy_repository.OrderUserRepository
import com.hifi.hifi_shopping.buy.datamodel.OrderProduct
import com.hifi.hifi_shopping.buy.datamodel.ProductFAQData
import com.hifi.hifi_shopping.buy.datamodel.ProductNormalReview
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class OrderItemViewModel: ViewModel() {

    var productMap = MutableLiveData<LinkedHashMap<String, OrderProduct>>()
    val tempHashMap =  LinkedHashMap<String, OrderProduct>()

    var productNoTitleImgMap = MutableLiveData<HashMap<String, Bitmap>>()
    val tempHashMap2 = HashMap<String, Bitmap>()

    var normalReviewMap = MutableLiveData<HashMap<String,ProductNormalReview>>()
    val tempReviewMap = HashMap<String, ProductNormalReview>()

    var productFAQ = MutableLiveData<HashMap<String,ProductFAQData>>()
    val tempFAQMap = HashMap<String, ProductFAQData>()

    fun getProductFAQUserInfo(idx: String){
        OrderUserRepository.getOrderUser(idx){
            for(c1 in it.result.children){
                tempFAQMap[c1.child("idx").value as String]?.nickname = c1.child("nickname").value as String
            }
            productFAQ.value = tempFAQMap
        }
    }

    fun getProductFAQ(idx: String){
        tempFAQMap.clear()
        OrderItemRepository.getProductFAQData(idx,{
            for(c1 in it.result.children){
                val productFAQData = ProductFAQData(
                    c1.child("writerIdx").value as String,
                    null,
                    c1.child("context").value as String
                )
                tempFAQMap[c1.child("writerIdx").value as String] = productFAQData
            }
        },{
            tempFAQMap.keys.forEach {
                getProductFAQUserInfo(it)
            }
        })
    }
    fun getProductReviewUserInfo(idx: String){
        OrderUserRepository.getOrderUser(idx){
            for(c1 in it.result.children){
                tempReviewMap[c1.child("idx").value as String]?.nickname = c1.child("nickname").value as String
            }
            normalReviewMap.value = tempReviewMap
        }
    }
    fun getProductNormalReview(idx: String){
        tempReviewMap.clear()
        OrderItemRepository.getProductNormalReview(idx,{
            for(c1 in it.result.children){
                val productNormalReview = ProductNormalReview(
                    c1.child("writerIdx").value as String,
                    null,
                    c1.child("context").value as String
                )
                tempReviewMap[c1.child("writerIdx").value as String] = productNormalReview
            }
        },{
            tempReviewMap.keys.forEach {
                getProductReviewUserInfo(it)
            }
        })
    }
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
                    } else {
                        OrderItemRepository.getProductImg(c1.child("imgSrc").value as String){
                            thread {
                                // 파일에 접근할 수 있는 경로를 이용해 URL 객체를 생성한다.
                                val url = URL(it.result.toString())
                                // 접속한다.
                                val httpURLConnection =
                                    url.openConnection() as HttpURLConnection
                                val bitmap =
                                    BitmapFactory.decodeStream(httpURLConnection.inputStream)
                                tempHashMap2[c1.child("omgOrder").value as String] = bitmap
                                productNoTitleImgMap.postValue(tempHashMap2)
                            }
                        }
                    }
                }
            }
        })
    }

}