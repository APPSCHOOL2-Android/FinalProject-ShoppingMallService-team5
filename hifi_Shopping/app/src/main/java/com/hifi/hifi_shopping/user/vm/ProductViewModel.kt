package com.hifi.hifi_shopping.user.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.user.model.AddressDataClass
import com.hifi.hifi_shopping.user.model.OrderDataClass
import com.hifi.hifi_shopping.user.model.ProductDataClass
import com.hifi.hifi_shopping.user.model.ReviewDataClass
import com.hifi.hifi_shopping.user.repository.AddressRepository
import com.hifi.hifi_shopping.user.repository.OrderRepository
import com.hifi.hifi_shopping.user.repository.ProductRepository
import com.hifi.hifi_shopping.user.repository.ReviewRepository

class ProductViewModel : ViewModel() {

    var productDataList = MutableLiveData<MutableList<ProductDataClass>>()

    fun getProductListByUser(userIdx : String){

        val tempList = mutableListOf<ProductDataClass>()

        ProductRepository.getProductInfoByIdx(userIdx) {
            for (c1 in it.result.children) {
                val category = c1.child("category").value as String
                val context = c1.child("context").value as String
                val idx = c1.child("idx").value as String
                val name = c1.child("name").value as String
                val pointAmount = c1.child("pointAmount").value as String
                val price = c1.child("price").value as String
                val sellerIdx = c1.child("sellerIdx").value as String

                val data = ProductDataClass(category, context, idx, name, pointAmount, price, sellerIdx)
                tempList.add(data)
            }
            productDataList.value = tempList
        }
    }
}