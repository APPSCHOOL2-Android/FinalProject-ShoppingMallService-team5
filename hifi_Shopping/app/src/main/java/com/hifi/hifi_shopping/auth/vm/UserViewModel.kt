package com.example.mini02_boardproject02.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel() : ViewModel() {
    var idx = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var pw = MutableLiveData<String>()
    var nickname = MutableLiveData<String>()
    var verify = MutableLiveData<String>()
    var phoneNum = MutableLiveData<String>()
    var profileImg = MutableLiveData<String>()


    fun reset() {
        idx.value = ""
        email.value = ""
        pw.value = ""
        nickname.value = ""
        verify.value = ""
        phoneNum.value = ""
        profileImg.value = ""
    }
}