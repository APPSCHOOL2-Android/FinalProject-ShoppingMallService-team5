package com.hifi.hifi_shopping.buy.buy_vm

import android.provider.Settings.Global
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.hifi.hifi_shopping.buy.buy_repository.OrderUserRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import java.util.UUID

data class AddressData(var idx: String, var userIdx: String, var receiver: String, var receiverPhoneNum: String, var address: String, var context: String)
data class OrderUserCoupon(val couponIdx: String, val used: Boolean, val userIdx: String)
data class PossibleCoupon(val idx: String, val categoryNum: String, val validData: String, val discountPercent: String, val verify: Boolean)
class OrderUserViewModel() : ViewModel() {
    // 인증 관련
    var nickname = MutableLiveData<String>()
    var verify = MutableLiveData<Boolean>()
    var phoneNum = MutableLiveData<String>()
    // 배송지 관련
    var addressData = MutableLiveData<AddressData>()
    var orderUserAddressList = MutableLiveData<MutableList<AddressData>>()
    var tempList = mutableListOf<AddressData>()
    // 쿠폰 관련
    val orderUserCouponList = MutableLiveData<MutableList<OrderUserCoupon>>()
    var tempList2 = mutableListOf<OrderUserCoupon>()
    val orderUserPossibleCouponList = MutableLiveData<MutableList<PossibleCoupon>>()
    var tempList3 = mutableListOf<PossibleCoupon>()

    fun getOrderUserCoupon(idx: String){
        tempList2.clear()
        OrderUserRepository.getOrderUserCoupon(idx,{
            for(c1 in it.result.children){
                val orderUserCoupon = OrderUserCoupon(
                    c1.child("couponIdx").value as String,
                    c1.child("used").value as String == "true",
                    c1.child("userIdx").value as String)
                if(orderUserCoupon.used) tempList2.add(orderUserCoupon)
            }
            orderUserCouponList.value = tempList2
        },{
            tempList3.clear()
            tempList2.forEach{
                OrderUserRepository.getOrderUserPossibleCoupon(it.couponIdx){
                    for(c1 in it.result.children){
                        val possibleCoupon = PossibleCoupon(
                            c1.child("idx").value as String,
                            c1.child("categoryNum").value as String,
                            c1.child("validDate").value as String,
                            c1.child("discountPercent").value as String,
                            c1.child("verify").value as String == "true",
                        )
                        Log.d("tttt", "$possibleCoupon")
                        if(possibleCoupon.verify) tempList3.add(possibleCoupon)
                    }
                    orderUserPossibleCouponList.value = tempList3
                }
            }
        })
    }

    fun setOrderUserAddress(num:Int){
        OrderUserRepository.orderUserSetAddress(orderUserAddressList.value!![num]){
            getOdderUserAddress(orderUserAddressList.value!![num].userIdx, num)
        }
    }

    fun lookOrderUserAddress(num:Int){
        addressData.value = orderUserAddressList.value!![num]
    }

    fun getOdderUserAddress(userIdx:String, num:Int){ // 가져오는 함수, 없다면 동일한 빈내용3개를 저장한다.
        tempList.clear()
        OrderUserRepository.orderUserGetAddress(userIdx){
            if(it.result.childrenCount == 0L){
                repeat(3){
                    val temp = AddressData(UUID.randomUUID().toString(), userIdx, "", "", " / ", "")
                    OrderUserRepository.addOrderUserAddress(temp){
                        tempList.add(temp)
                    }
                }
            } else{
                for(u1 in it.result.children){
                    val temp = AddressData(
                        u1.child("idx").value as String,
                        u1.child("userIdx").value as String,
                        u1.child("receiver").value as String,
                        u1.child("receiverPhoneNum").value as String,
                        u1.child("address").value as String,
                        u1.child("context").value as String
                    )
                    tempList.add(temp)
                }
            }
            orderUserAddressList.value = tempList
            addressData.value = orderUserAddressList.value!![num]
        }
    }


    fun orderUserAuthCheck(userIdx:String){
        OrderUserRepository.getOrderUser(userIdx){
            for(u1 in it.result.children){
                nickname.value = u1.child("nickname").value as String
                verify.value = u1.child("verify").value == "true"
                phoneNum.value = u1.child("phoneNum").value as String
            }
        }
    }

    fun setOrderUserAuth(userIdx:String){
        OrderUserRepository.setUserAuth(userIdx){
            verify.value = true
        }
    }
}