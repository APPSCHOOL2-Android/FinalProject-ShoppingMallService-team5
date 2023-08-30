package com.hifi.hifi_shopping.parcel.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hifi.hifi_shopping.parcel.RowParcelItemClass
import com.hifi.hifi_shopping.parcel.repository.ParcelRepository

import kotlinx.coroutines.launch


class ParcelViewModel() : ViewModel() {
    val packingParcelList = MutableLiveData<MutableList<RowParcelItemClass>>()
    val shippingParcelList = MutableLiveData<MutableList<RowParcelItemClass>>()
    val arrivingParcelList = MutableLiveData<MutableList<RowParcelItemClass>>()
    fun getParcelByUserIdx(userIdx:String) {
        viewModelScope.launch {
            val parcelListInfo = ParcelRepository.getParcelInfoByUserIdx(userIdx)
            val tempPackingParcelList = mutableListOf<RowParcelItemClass>()
            val tempShippingParcelList = mutableListOf<RowParcelItemClass>()
            val tempArrivingParcelList = mutableListOf<RowParcelItemClass>()
            if (parcelListInfo != null) {
                for (parcel in parcelListInfo!!.children) {
                    val productIdx = parcel.child("productIdx").value as String
                    val parcelStatus = parcel.child("status").value as String
                    val parcelDate = parcel.child("date").value as String
                    val newRowParcelItem = RowParcelItemClass(
                        productIdx, "로딩 중", "로딩 중", null,
                        parcelStatus, parcelDate
                    )
                    if(parcelStatus == "PACKING"){
                        tempPackingParcelList.add(newRowParcelItem)
                    }else if(parcelStatus == "SHIPPING"){
                        tempShippingParcelList.add(newRowParcelItem)
                    }else{
                        tempArrivingParcelList.add(newRowParcelItem)
                    }
                }
            }
            packingParcelList.value = tempPackingParcelList
            shippingParcelList.value = tempShippingParcelList
            arrivingParcelList.value = tempArrivingParcelList
        }
    }
}