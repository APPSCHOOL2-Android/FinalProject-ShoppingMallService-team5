package com.example.mini02_boardproject02.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.auth.model.UserDataClass
import com.hifi.hifi_shopping.auth.repository.UserRepository

class UserViewModel() : ViewModel() {
    var userData =  MutableLiveData<UserDataClass>()
    fun getUser(userEmail:String) {
        UserRepository.getUserInfoByUserEmail(userEmail) {
            for (c1 in it.result.children) {
                val idx = c1.child("idx").value as String
                val email = c1.child("email").value as String
                val pw = c1.child("pw").value as String
                val nickname = c1.child("nickname").value as String
                val verify = c1.child("verify").value as String
                val phoneNum = c1.child("phoneNum").value as String
                val profileImg = c1.child("profileImg").value as String

                // 데이터 클래스 객체 생성 및 리스트에 추가
                userData.value = UserDataClass(idx, email, pw, nickname, verify, phoneNum, profileImg)

            }
        }
    }
}