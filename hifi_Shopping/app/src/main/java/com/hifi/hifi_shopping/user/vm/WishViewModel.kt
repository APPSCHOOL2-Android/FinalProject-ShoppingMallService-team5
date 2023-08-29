package com.hifi.hifi_shopping.user.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.user.model.ProductDataClass
import com.hifi.hifi_shopping.user.model.WishDataClass
import com.hifi.hifi_shopping.user.repository.ProductRepository
import com.hifi.hifi_shopping.user.repository.WishRepository

class WishViewModel : ViewModel() {

    var wishDataList = MutableLiveData<MutableList<WishDataClass>>()
    var productDataList = MutableLiveData<MutableList<ProductDataClass>>()


    fun getWishListByUser(useridx: String) {

        val tempList = mutableListOf<WishDataClass>()
        val tempList2 = mutableListOf<ProductDataClass>()


        WishRepository.getWishAllByUserIdx(useridx) {
            for (c1 in it.result.children) {
                val userIdx = c1.child("userIdx").value as String
                val productIdx = c1.child("productIdx").value as String
                val data = WishDataClass(userIdx, productIdx)
                tempList.add(data)
            }
            wishDataList.value = tempList
            Log.d("위시", productDataList.value.toString())


            wishDataList.value?.forEach {
                // 제품 정보를 가져옴
                ProductRepository.getProductInfoByIdx(it.productIdx) {
                    for (c2 in it.result.children) {
                        val category = c2.child("category").value as String
                        val context = c2.child("context").value as String
                        val idx = c2.child("idx").value as String
                        val name = c2.child("name").value as String
                        val pointAmount = c2.child("pointAmount").value as String
                        val price = c2.child("price").value as String
                        val sellerIdx = c2.child("sellerIdx").value as String

                        val data = ProductDataClass(
                            category,
                            context,
                            idx,
                            name,
                            pointAmount,
                            price,
                            sellerIdx
                        )
                        tempList2.add(data)
                    }
                    productDataList.value = tempList2
                    Log.d("위시2", productDataList.value.toString())
                }
            }


        }
    }

}