package com.hifi.hifi_shopping.user.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.user.model.AddressDataClass
import com.hifi.hifi_shopping.user.model.ReviewDataClass
import com.hifi.hifi_shopping.user.repository.AddressRepository
import com.hifi.hifi_shopping.user.repository.ReviewRepository

class AddressViewModel : ViewModel() {

    var addressDataList = MutableLiveData<MutableList<AddressDataClass>>()
    fun getAddressListByUser(userIdx : String){

        val tempList = mutableListOf<AddressDataClass>()

        AddressRepository.getAddressAllByUserIdx(userIdx) {
            for (c1 in it.result.children) {
                val idx = c1.child("idx").value as String
                val userIdx = c1.child("userIdx").value as String
                val receiver = c1.child("receiver").value as String?
                val receiverPhoneNum = c1.child("receiverPhoneNum").value as String?
                val address = c1.child("address").value as String?
                val context = c1.child("context").value as String?

                val data = AddressDataClass(idx, userIdx, receiver, receiverPhoneNum, address, context)
                tempList.add(data)
            }
            addressDataList.value = tempList
        }
    }
}