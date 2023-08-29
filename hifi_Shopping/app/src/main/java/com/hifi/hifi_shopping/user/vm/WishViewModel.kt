package com.hifi.hifi_shopping.user.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.user.model.ProductDataClass
import com.hifi.hifi_shopping.user.model.WishDataClass
import com.hifi.hifi_shopping.user.repository.ProductRepository
import com.hifi.hifi_shopping.user.repository.WishRepository

class WishViewModel : ViewModel() {

    var wishDataList = MutableLiveData<MutableList<WishDataClass>>()


    fun getWishListByUser(useridx: String) {

        val tempList = mutableListOf<WishDataClass>()

        WishRepository.getWishAllByUserIdx(useridx) {
            for (c1 in it.result.children) {
                val userIdx = c1.child("userIdx").value as String
                val productIdx = c1.child("productIdx").value as String
                val data = WishDataClass(userIdx, productIdx)
                tempList.add(data)
            }
            wishDataList.value = tempList
        }
    }

}