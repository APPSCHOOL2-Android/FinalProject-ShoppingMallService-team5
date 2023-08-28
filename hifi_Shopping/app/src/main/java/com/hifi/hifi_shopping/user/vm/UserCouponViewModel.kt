package com.hifi.hifi_shopping.user.vm

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hifi.hifi_shopping.user.model.CouponDataClass
import com.hifi.hifi_shopping.user.model.UserCouponDataClass
import com.hifi.hifi_shopping.user.repository.CouponRepository
import com.hifi.hifi_shopping.user.repository.UserCouponRepository
import com.hifi.hifi_shopping.user.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserCouponViewModel : ViewModel() {

    var userCouponDataList = MutableLiveData<MutableList<UserCouponDataClass>>()
    var userCouponEnabledDataList = MutableLiveData<MutableList<UserCouponDataClass>>()
    var couponInfoList = MutableLiveData<MutableList<CouponDataClass>>()
    var couponEnabledList = MutableLiveData<MutableList<CouponDataClass>>()
    var couponDisabledList = MutableLiveData<MutableList<CouponDataClass>>()
    var couponAllList = MutableLiveData<MutableList<CouponDataClass>>()


    fun getUserCouponAll(useridx: String) {

        val tempList = mutableListOf<UserCouponDataClass>()
        val tempList2 = mutableListOf<CouponDataClass>()
        viewModelScope.launch(Dispatchers.IO) {
            UserCouponRepository.getUserCouponListByUserIdx(useridx) {
                for (c1 in it.result.children) {
                    val userIdx = c1.child("userIdx").value as String
                    val used = c1.child("used").value as String
                    val couponIdx = c1.child("couponIdx").value as String

                    val uc1 = UserCouponDataClass(userIdx, couponIdx, used)
                    tempList.add(uc1)
                }
                userCouponDataList.value = tempList
                Log.d("쿠폰1", userCouponDataList.value.toString())

                userCouponDataList.value?.forEach {
                    CouponRepository.getCouponInfo(it.couponIdx) {
                        for (c2 in it.result.children) {
                            val categoryNum = c2.child("categoryNum").value as String
                            val discountPercent = c2.child("discountPercent").value as String
                            val idx = c2.child("idx").value as String
                            val validDate = c2.child("validDate").value as String
                            val verify = c2.child("verify").value as String
                            val ci = CouponDataClass(
                                idx,
                                categoryNum,
                                validDate,
                                discountPercent,
                                verify
                            )

                            tempList2.add(ci)
                        }
                        couponInfoList.value = tempList2
                        Log.d("쿠폰", couponInfoList.value.toString())
                    }
                }
            }

        }
    }

    fun getUserCouponEnabledAll(useridx: String) {

        val tempList = mutableListOf<UserCouponDataClass>()
        val tempList2 = mutableListOf<CouponDataClass>()
        viewModelScope.launch(Dispatchers.IO) {
            UserCouponRepository.getUserCouponListByUserIdx(useridx) {
                for (c1 in it.result.children) {
                    if (c1.child("used").value.toString().equals("true")) {
                        val userIdx = c1.child("userIdx").value as String
                        val used = c1.child("used").value as String
                        val couponIdx = c1.child("couponIdx").value as String

                        val uc1 = UserCouponDataClass(userIdx, couponIdx, used)
                        tempList.add(uc1)
                    }
                }
                userCouponEnabledDataList.value = tempList
                Log.d("쿠폰3", userCouponEnabledDataList.value.toString())

                userCouponEnabledDataList.value?.forEach {
                    CouponRepository.getCouponInfo(it.couponIdx) {
                        for (c2 in it.result.children) {
                            val categoryNum = c2.child("categoryNum").value as String
                            val discountPercent = c2.child("discountPercent").value as String
                            val idx = c2.child("idx").value as String
                            val validDate = c2.child("validDate").value as String
                            val verify = c2.child("verify").value as String
                            val ci = CouponDataClass(
                                idx,
                                categoryNum,
                                validDate,
                                discountPercent,
                                verify
                            )

                            tempList2.add(ci)
                        }
                        couponEnabledList.value = tempList2
                        Log.d("쿠폰4", couponEnabledList.value.toString())
                    }
                }
            }

        }
    }

    fun getCouponAll() {
        val tempList = mutableListOf<CouponDataClass>()
        CouponRepository.getCouponAll {
            for (c2 in it.result.children) {
                val categoryNum = c2.child("categoryNum").value as String
                val discountPercent = c2.child("discountPercent").value as String
                val idx = c2.child("idx").value as String
                val validDate = c2.child("validDate").value as String
                val verify = c2.child("verify").value as String
                val ci = CouponDataClass(
                    idx,
                    categoryNum,
                    validDate,
                    discountPercent,
                    verify
                )

                tempList.add(ci)
            }
            couponAllList.value = tempList
        }
    }


    fun getUserCouponDisabledAll(useridx: String) {

        val tempList = mutableListOf<UserCouponDataClass>()
        val tempList2 = mutableListOf<CouponDataClass>()
        viewModelScope.launch(Dispatchers.IO) {
            UserCouponRepository.getUserCouponListByUserIdx(useridx) {
                for (c1 in it.result.children) {
                    if (c1.child("used").value.toString().equals("true")) {
                        val userIdx = c1.child("userIdx").value as String
                        val used = c1.child("used").value as String
                        val couponIdx = c1.child("couponIdx").value as String

                        val uc1 = UserCouponDataClass(userIdx, couponIdx, used)
                        tempList.add(uc1)
                    }
                }
                userCouponEnabledDataList.value = tempList
                Log.d("쿠폰5", userCouponEnabledDataList.value.toString())

                userCouponEnabledDataList.value?.forEach {userCoupon ->
                    CouponRepository.getCouponAll {
                        for (c2 in it.result.children) {
                            if(c2.child("idx").value != userCoupon.couponIdx) {
                                val categoryNum = c2.child("categoryNum").value as String
                                val discountPercent = c2.child("discountPercent").value as String
                                val idx = c2.child("idx").value as String
                                val validDate = c2.child("validDate").value as String
                                val verify = c2.child("verify").value as String
                                val ci = CouponDataClass(
                                    idx,
                                    categoryNum,
                                    validDate,
                                    discountPercent,
                                    verify
                                )

                                tempList2.add(ci)
                            }
                        }
                        couponDisabledList.value = tempList2
                        Log.d("쿠폰6", couponDisabledList.value.toString())
                    }
                }
            }

        }
    }
}


