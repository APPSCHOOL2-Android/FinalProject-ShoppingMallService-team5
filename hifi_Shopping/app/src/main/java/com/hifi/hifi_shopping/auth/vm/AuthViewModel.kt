package com.hifi.hifi_shopping.auth.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.auth.model.UserDataClass
import com.hifi.hifi_shopping.auth.repository.AuthRepository

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository(application)

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean> = _registrationResult

    private val INVALID_NICKNAME_CHARACTERS = listOf(
        "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "+", "=", "[", "]", "{", "}",
        "|", "\\", ":", ";", "\"", "'", "<", ">", ",", ".", "/", "?"
    )

    // viewModel
    val userData = MutableLiveData<UserDataClass>()
    fun getUserByAuth() {
        AuthRepository.getUserByAuth {
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

    fun registerUserData(email: String, password: String, nickname:String, phoneNum:String){
        AuthRepository.registerUserData(email, password){
            val user = it.user
            val userId = user?.uid ?: ""
            val profileImgRef = "sample_img"
            val verify = "false"
            val newUser = UserDataClass(userId, email, password, nickname, verify, phoneNum, profileImgRef)
            userData.value = newUser
        }
    }


    // AuthLoginFragment의 로그인 함수
    fun loginUser(email: String, password: String) {
        authRepository.loginUser(email, password) { success ->
            _loginResult.value = success
        }
    }

    // AuthJoinFragment의 계정 등록 함수
    fun registerUser(email: String, password: String, nickname: String, passwordCheck: String, imageByteArray: ByteArray) {
        authRepository.registerUser(email, password, nickname, imageByteArray,
            onSuccess = {
                _registrationResult.value = true
            },
            onError = { errorMessage ->
                _registrationResult.value = false
            }
        )
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