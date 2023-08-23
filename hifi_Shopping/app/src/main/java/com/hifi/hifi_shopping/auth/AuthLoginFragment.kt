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
    lateinit var googleSignInHelper: GoogleSignInHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthLoginBinding = FragmentAuthLoginBinding.inflate(inflater)
        authActivity = activity as AuthActivity

        // GoogleSignInHelper 인스턴스 생성
        googleSignInHelper = GoogleSignInHelper(requireActivity(), authActivity.auth!!)

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
            // 일반 로그인 버튼 클릭
            buttonAuthLogin.setOnClickListener{
                // 사용자 입력 정보를 가져옵니다.
                val email = textInputEditTextLoginUserId.text.toString()
                val password = textInputEditTextLoginUserPw.text.toString()

                // 사용자 정보를 파이어베이스에 전달하고 로그인을 시도합니다.
                authActivity.auth?.signInWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // 로그인 성공 시
                            val successMessage = "일반 로그인 성공" // 토스트에 출력할 메시지
                            Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show()
                            // 화면전환 처리 필요
                            // authActivity.replaceFragment(...)
                        } else {
                            // 로그인 실패 시
                            // 예외 처리 필요
                            val exception = task.exception
                            val errorMessage = exception?.message ?: "로그인 실패"
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            // 구글 로그인 버튼 클릭
            buttonAuthGoogleLogin.setOnClickListener {
                // GoogleSignInHelper를 통한 구글 로그인 시작
                googleSignInHelper.signIn()
            }
        }

        return fragmentAuthLoginBinding.root
    }
}