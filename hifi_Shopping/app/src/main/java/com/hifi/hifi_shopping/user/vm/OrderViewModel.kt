package com.hifi.hifi_shopping.user.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.user.model.AddressDataClass
import com.hifi.hifi_shopping.user.model.OrderDataClass
import com.hifi.hifi_shopping.user.model.ReviewDataClass
import com.hifi.hifi_shopping.user.repository.AddressRepository
import com.hifi.hifi_shopping.user.repository.OrderRepository
import com.hifi.hifi_shopping.user.repository.ReviewRepository

class OrderViewModel : ViewModel() {

    var orderDataList = MutableLiveData<MutableList<OrderDataClass>>()
    fun getOrderListByUser(userIdx : String){

        val tempList = mutableListOf<OrderDataClass>()

        OrderRepository.getOrderAllByUserIdx(userIdx) {
            for (c1 in it.result.children) {
                val addressIdx = c1.child("addressIdx").value as String
                val buyerIdx = c1.child("buyerIdx").value as String
                val couponIdx = c1.child("couponIdx").value as String?
                val date = c1.child("date").value as String
                val idx = c1.child("idx").value as String
                val price = c1.child("price").value as String
                val productIdx = c1.child("productIdx").value as String
                val status = c1.child("status").value as String

                val data = OrderDataClass(addressIdx, buyerIdx, couponIdx, date, idx, price, productIdx, status)
                tempList.add(data)
            }
            orderDataList.value = tempList
        }
    }
}