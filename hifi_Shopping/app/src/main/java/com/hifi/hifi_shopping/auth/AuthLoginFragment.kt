package com.hifi.hifi_shopping.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.hifi.hifi_shopping.auth.vm.AuthViewModel
import com.hifi.hifi_shopping.databinding.FragmentAuthLoginBinding

class AuthLoginFragment : Fragment() {

    lateinit var fragmentAuthLoginBinding: FragmentAuthLoginBinding
    lateinit var authActivity : AuthActivity
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthLoginBinding = FragmentAuthLoginBinding.inflate(inflater)
        authActivity = activity as AuthActivity

        fragmentAuthLoginBinding.run{

            // 회원가입 텍스트 클릭 => JoinFragment로 교체
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
                    authViewModel.loginUser(email, password)

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

        // 로그인 결과를 옵저빙하여 UI 업데이트
        authViewModel.loginResult.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                // 로그인 성공: 토스트 메시지 출력
                // 이 부분은 뷰 모델에서 관찰되었을 때만 호출되어야 합니다.
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle("로그인 성공")
                    .setMessage("화면 이동 및 데이터 전달을 구성 필요.")
                    .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
                    .create()
                alertDialog.show()
           } else {
                // 로그인 실패: 다이얼로그 출력
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle("로그인 실패")
                    .setMessage("이메일 또는 비밀번호가 올바르지 않습니다.")
                    .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
                    .create()
                alertDialog.show()
            }
        })

        return fragmentAuthLoginBinding.root
    }
}