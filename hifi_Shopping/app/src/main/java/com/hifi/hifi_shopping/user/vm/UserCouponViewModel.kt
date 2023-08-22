package com.hifi.hifi_shopping.user.vm

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.user.model.CouponDataClass
import com.hifi.hifi_shopping.user.model.UserCouponDataClass
import com.hifi.hifi_shopping.user.repository.CouponRepository
import com.hifi.hifi_shopping.user.repository.UserCouponRepository
import com.hifi.hifi_shopping.user.repository.UserRepository

class UserCouponViewModel : ViewModel() {

    var userCouponDataList = MutableLiveData<MutableList<UserCouponDataClass>>()
    var couponInfoList = MutableLiveData<MutableList<CouponDataClass>>()

    var userCouponUserIdx = MutableLiveData<String>()
    var userCouponUsed = MutableLiveData<Boolean>()
    var userCouponCouponIdx = MutableLiveData<String>()

    fun getUserCouponAll(useridx:String){

        val tempList = mutableListOf<UserCouponDataClass>()
        val tempList2 = mutableListOf<CouponDataClass>()

        UserCouponRepository.getUserCouponListByUserIdx(useridx){
            for(c1 in it.result.children){
                val userIdx = c1.child("userIdx").value as String
                val used = c1.child("used").value as Boolean
                val couponIdx = c1.child("couponIdx").value as String

                val uc1 = UserCouponDataClass(userIdx,couponIdx,used)
                tempList.add(uc1)

                CouponRepository.getCouponInfo(couponIdx){
                    for(c2 in it.result.children){
                        val categoryNum = c1.child("categoryNum").value as String
                        val discountPercent = c1.child("discountPercent").value as Long
                        val idx = c1.child("idx").value as String
                        val validDate = c1.child("validDate").value as String
                        val verify = c1.child("verify").value as Boolean
                        val ci = CouponDataClass(idx,categoryNum,validDate,discountPercent,verify)

                        tempList2.add(ci)
                    }

                }
            }

            userCouponDataList.value = tempList
            couponInfoList.value = tempList2
        }
    }
}