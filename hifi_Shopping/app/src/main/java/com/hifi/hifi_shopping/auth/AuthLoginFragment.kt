package com.hifi.hifi_shopping.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hifi.hifi_shopping.databinding.FragmentAuthLoginBinding

class AuthLoginFragment : Fragment() {

    lateinit var fragmentAuthLoginBinding: FragmentAuthLoginBinding
    lateinit var authActivity : AuthActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthLoginBinding = FragmentAuthLoginBinding.inflate(inflater)
        authActivity = activity as AuthActivity

        fragmentAuthLoginBinding.run{

            // 회원가입 텍스트 클릭 => LoginFragment로 교체
            textViewAuthJoin.run{
                setOnClickListener{
                    authActivity.replaceFragment(AuthActivity.AUTH_JOIN_FRAGMENT, true, null)
                }
            }
            // 비번찾기 텍스트 클릭 => FindPwFragment
            textViewAuthFindPw.run{
                setOnClickListener{
                    authActivity.replaceFragment(AuthActivity.AUTH_FIND_PW_FRAGMENT, true, null)
                }
            }

            // 로그인 버튼 클릭 시 포커스와 키보드 제어
            buttonAuthLogin.setOnClickListener {
                // 사용자 입력 정보를 가져옵니다.
                val email = textInputEditTextLoginUserId.text.toString()
                val password = textInputEditTextLoginUserPw.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    // 이메일이나 비밀번호가 비어있다면 해당 에디트 텍스트로 포커스를 올립니다.
                    if (email.isEmpty()) {
                        textInputEditTextLoginUserId.requestFocus()
                        authActivity.showSoftInput(textInputEditTextLoginUserId)
                    } else {
                        textInputEditTextLoginUserPw.requestFocus()
                        authActivity.showSoftInput(textInputEditTextLoginUserPw)
                    }
                } else {
                    // 이메일과 비밀번호가 모두 입력되었다면 로그인 시도
                    authActivity.loginUser(email, password)

                    // 포커스와 키보드 클리어
                    textInputEditTextLoginUserId.clearFocus()
                    textInputEditTextLoginUserPw.clearFocus()
                }
            }

            // 에디트 텍스트 클릭 시 포커스와 키보드
            textInputEditTextLoginUserId.setOnClickListener {
                authActivity.showSoftInput(it)
            }
        }

        return fragmentAuthLoginBinding.root
    }
}