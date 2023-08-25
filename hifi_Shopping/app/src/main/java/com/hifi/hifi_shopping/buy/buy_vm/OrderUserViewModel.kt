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

data class AddressData(var idx: String, var userIdx: String, var receiver:String, var receiverPhoneNum:String, var address:String, var context:String)
class OrderUserViewModel() : ViewModel() {
    // 인증 관련
    var nickname = MutableLiveData<String>()
    var verify = MutableLiveData<Boolean>()
    var phoneNum = MutableLiveData<String>()
    // 배송지 관련
    var addressData = MutableLiveData<AddressData>()
    var orderUserAddressList = MutableLiveData<MutableList<AddressData>>()
    var tempList = mutableListOf<AddressData>()


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