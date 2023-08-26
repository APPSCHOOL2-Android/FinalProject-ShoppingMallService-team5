package com.hifi.hifi_shopping.user.vm

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hifi.hifi_shopping.user.model.ProductDataClass
import com.hifi.hifi_shopping.user.model.TempData
import com.hifi.hifi_shopping.user.repository.ProductImgRepository
import com.hifi.hifi_shopping.user.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class ProductViewModel : ViewModel() {
    var productDataList = MutableLiveData<MutableList<ProductDataClass>>()
    var productDataFiveList = MutableLiveData<MutableList<ProductDataClass>>()
    var productDataByIdx = MutableLiveData<ProductDataClass>()

    fun getProductList() {

        val tempList = mutableListOf<ProductDataClass>()

        ProductRepository.getProductAll {
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
                tempList.add(data)
            }
            productDataList.value = tempList
        }
    }

    fun getProductInfoByIdx(idx: String) {

        ProductRepository.getProductInfoByIdx(idx) {
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
                productDataByIdx.value = data
            }

        }
    }
}



