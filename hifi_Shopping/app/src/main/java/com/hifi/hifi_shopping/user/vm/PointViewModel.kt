package com.hifi.hifi_shopping.user.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.user.model.PointDataClass
import com.hifi.hifi_shopping.user.model.UserDataClass
import com.hifi.hifi_shopping.user.repository.PointRepository

class PointViewModel  : ViewModel() {

    var pointDataList = MutableLiveData<MutableList<PointDataClass>>()

    fun getPointList(userIdx : String){

        val tempList = mutableListOf<PointDataClass>()

        PointRepository.getPointListByUserEmail(userIdx) {
            for(c1 in it.result.children) {
                val userIdx = c1.child("userIdx").value as String
                val amount = (c1.child("amount").value as String).toLong()
                val date = c1.child("date").value as String
                val context = c1.child("context").value as String

                val pointData = PointDataClass(userIdx, amount, date, context)
                tempList.add(pointData)

            }

            pointDataList.value = tempList
        }
    }


}