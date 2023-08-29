package com.hifi.hifi_shopping.auth.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.auth.model.UserDataClass
import com.hifi.hifi_shopping.auth.repository.AuthTestRepository

class AuthTestViewModel() : ViewModel() {
    val userData = MutableLiveData<UserDataClass>()

    private val INVALID_NICKNAME_CHARACTERS = listOf(
        "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "+", "=", "[", "]", "{", "}",
        "|", "\\", ":", ";", "\"", "'", "<", ">", ",", ".", "/", "?"
    )

    // AuthLoginFragment의 로그인 함수
    fun loginUser(email: String, password: String) {
        AuthTestRepository.loginUser(email, password) {
            val userUid = it.user?.uid
            Log.d("testaaa", "${userUid}")
            if(userUid != null) {
                AuthTestRepository.getUserInfoByUserId(userUid) { currentUser ->
                    val userIdx = currentUser.result.child("idx").value as String
                    val userNickname = currentUser.result.child("nickname").value as String
                    val userPhoneNum = currentUser.result.child("phoneNum").value as String
                    val userProfileImg = currentUser.result.child("profileImg").value as String
                    val userVerify = currentUser.result.child("verify").value as String
                    val loginUser = UserDataClass(
                        userIdx, email, password, userNickname, userVerify,
                        userPhoneNum, userProfileImg
                    )
                    userData.value = loginUser
                }
            }
        }
    }

    // AuthJoinFragment의 계정 등록 함수
    fun registerUser(email: String, password: String, nickname:String, phoneNum:String){
        AuthTestRepository.registerUser(email, password){
            val user = it.user
            val userId = user?.uid ?:""
            val profileImgRef = "sample_img"
            val verify = "false"
            val newUser = UserDataClass(userId, email, password, nickname, verify, phoneNum, profileImgRef)
            AuthTestRepository.addUserInfo(userId, newUser){
                Log.d("testaaa", "로그인 성공")
            }
            userData.value = newUser
        }
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    fun isNicknameValid(nickname: String): Boolean {
        return nickname.length in 2..12 && !INVALID_NICKNAME_CHARACTERS.any { nickname.contains(it) }
    }

}