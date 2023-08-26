package com.hifi.hifi_shopping.auth.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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