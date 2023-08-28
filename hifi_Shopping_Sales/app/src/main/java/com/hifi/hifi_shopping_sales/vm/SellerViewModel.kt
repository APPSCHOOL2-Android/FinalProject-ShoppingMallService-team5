package com.hifi.hifi_shopping_sales.vm

import android.content.DialogInterface
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.hifi.hifi_shopping_sales.repository.SellerRepository
import com.hifi.hifi_shopping_sales.seller.SellerActivity
import com.hifi.hifi_shopping_sales.seller.SellerClass

class SellerViewModel : ViewModel() {
    val companyName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val idx = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val pw = MutableLiveData<String>()

    fun getLoginSellerInfo(loginSellerId:String, loginSellerPw:String) : Int {
        var errorCode = 0
        SellerRepository.getUserInfoByUserId(loginSellerId){
            if(it.result.exists() == false) {
                errorCode = 1
                return@getUserInfoByUserId
            }
            else {
                for(c1 in it.result.children){
                    // 가져온 데이터에서 비밀번호를 가져온다.
                    val password = c1.child("pw").value as String

                    // 입력한 비밀번호와 현재 계정의 비밀번호가 다르다면
                    if(loginSellerPw != password){
                        errorCode = 2
                        return@getUserInfoByUserId
                    }
                    // 입력한 비밀번호와 현재 계정의 비밀번호가 같다면
                    else {
                        // 로그인한 사용자 정보를 가져온다.
                        idx.value = c1.child("idx").value as String
                        email.value = c1.child("email").value as String
                        pw.value = c1.child("pw").value as String
                        companyName.value = c1.child("companyName").value as String
                        name.value = c1.child("name").value as String
                    }
                }
            }
        }
        return errorCode
    }
}