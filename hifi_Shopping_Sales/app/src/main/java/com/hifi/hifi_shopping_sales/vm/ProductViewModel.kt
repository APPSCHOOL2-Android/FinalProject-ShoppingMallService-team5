package com.hifi.hifi_shopping_sales.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping_sales.repository.ImageRepository
import com.hifi.hifi_shopping_sales.repository.ProductRepository
import com.hifi.hifi_shopping_sales.seller.ImgClass
import com.hifi.hifi_shopping_sales.seller.ProductClass
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class ProductViewModel() : ViewModel() {
    val loadImgBitmap = MutableLiveData<Pair<String,Bitmap>>()
    val productList = MutableLiveData<HashMap<String,ProductClass>>()
    init{
        productList.value = HashMap<String,ProductClass>()
    }

    // 게시글 목록
    fun getProductAll(){
        val tempList = HashMap<String,ProductClass>()
        ProductRepository.getProductAll("0") {
            for(c1 in it.result.children){
                val pidx = c1.child("idx").value as String
                val pcategory = c1.child("category").value as String
                val pcontext = c1.child("context").value as String
                val pname = c1.child("name").value as String
                val ppointAmount = c1.child("pointAmount").value as String
                val pprice = c1.child("price").value as String
                val psellerIdx = c1.child("sellerIdx").value as String
                val p1 = ProductClass(pidx, pcategory, pcontext, pprice, pname, ppointAmount, psellerIdx, null)
                tempList[pidx] = p1
            }
            productList.value = tempList
        }
    }

    // 게시글 읽기 화면
    fun getProductImgListByIdx(idx:String){
        // 게시글 데이터를 가져온다.
        ProductRepository.getProductImgListByIdx(idx){
            for(c1 in it.result.children){
                // 게시글 제목
                val def = c1.child("default").value as String
                val order = c1.child("omgOrder").value as String
                val fileName = c1.child("imgSrc").value as String
                ProductRepository.getProductImgBitmapByFileName(fileName) {
                    thread {
                        // 파일에 접근할 수 있는 경로를 이용해 URL 객체를 생성한다.
                        val url = URL(it.result.toString())
                        // 접속한다.
                        val httpURLConnection = url.openConnection() as HttpURLConnection
                        // 이미지 객체를 생성한다.
                        val bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)
                        val newImgList = ImgClass(order, def, fileName, bitmap, idx)
                        productList?.value?.get(idx)?.imgList?.add(newImgList)
                        if(def == "true" && order == "1") {
                            loadImgBitmap.postValue(Pair(idx, bitmap))
                        }
                    }
                }
            }
        }
    }
}