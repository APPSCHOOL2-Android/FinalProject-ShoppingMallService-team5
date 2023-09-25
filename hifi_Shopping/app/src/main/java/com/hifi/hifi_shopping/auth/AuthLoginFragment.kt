package com.hifi.hifi_shopping.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hifi.hifi_shopping.auth.vm.AuthTestViewModel
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.databinding.FragmentAuthLoginBinding

class AuthLoginFragment : Fragment() {

    lateinit var fragmentAuthLoginBinding: FragmentAuthLoginBinding
    lateinit var authActivity: AuthActivity
    lateinit var authTestViewModel: AuthTestViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAuthLoginBinding = FragmentAuthLoginBinding.inflate(inflater)
        authActivity = activity as AuthActivity

        // UI 요소에 대한 리스너 설정
        setupUiListeners()
        authTestViewModel = ViewModelProvider(authActivity)[AuthTestViewModel::class.java]

        // 로그인 결과를 관찰하여 UI 업데이트

        authTestViewModel.run{
            userData.observe(viewLifecycleOwner){
                val intent = Intent(authActivity, CategoryActivity::class.java)
                intent.putExtra("userEmail", it.email)
                intent.putExtra("userIdx", it.idx)
                intent.putExtra("userNickname", it.nickname)
                intent.putExtra("userPw", it.pw)
                intent.putExtra("userVerify", it.verify)
                intent.putExtra("userPhoneNum", it.phoneNum)
                intent.putExtra("userProfileImg", it.profileImg)

                startActivity(intent)
            }
        }

        return fragmentAuthLoginBinding.root
    }

    // UI 요소에 리스너를 설정하는 함수
    private fun setupUiListeners() {
        // 회원가입 텍스트 클릭 시 JoinFragment로 교체
        fragmentAuthLoginBinding.textViewAuthJoin.setOnClickListener {
            authActivity.replaceFragment(AuthActivity.AUTH_JOIN_FRAGMENT, true, null)
        }

        // 비밀번호 찾기 텍스트 클릭 시 FindPwFragment로 교체
        fragmentAuthLoginBinding.textViewAuthFindPw.setOnClickListener {
            authActivity.replaceFragment(AuthActivity.AUTH_FIND_PW_FRAGMENT, true, null)
        }

        // 로그인 버튼 클릭 시 로그인 처리 함수 호출
        fragmentAuthLoginBinding.buttonAuthLogin.setOnClickListener {
            handleLoginButtonClick()
        }

        // 이메일 입력 텍스트 클릭 시 소프트 키보드 표시
        fragmentAuthLoginBinding.textInputEditTextLoginUserId.setOnClickListener {
            authActivity.showSoftInput(it)
        }
    }

    // 로그인 버튼 클릭 처리 함수
    private fun handleLoginButtonClick() {
        val email = fragmentAuthLoginBinding.textInputEditTextLoginUserId.text.toString()
        val password = fragmentAuthLoginBinding.textInputEditTextLoginUserPw.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) {
                // 이메일이 비어있다면 이메일 입력란에 포커스 및 키보드 표시
                fragmentAuthLoginBinding.textInputEditTextLoginUserId.requestFocus()
                authActivity.showSoftInput(fragmentAuthLoginBinding.textInputEditTextLoginUserId)
                showLoginFailureDialog("이메일을 입력해주세요!!🥲")
            } else {
                // 비밀번호가 비어있다면 비밀번호 입력란에 포커스 및 키보드 표시
                fragmentAuthLoginBinding.textInputEditTextLoginUserPw.requestFocus()
                authActivity.showSoftInput(fragmentAuthLoginBinding.textInputEditTextLoginUserPw)
                showLoginFailureDialog("비밀번호를 입력해주세요!!🥲")
            }
        } else {
            // 이메일과 비밀번호가 입력되었다면 로그인 처리 함수 호출
            authTestViewModel.loginUser(email, password, authActivity)
            // 포커스와 키보드 클리어
            fragmentAuthLoginBinding.textInputEditTextLoginUserId.clearFocus()
            fragmentAuthLoginBinding.textInputEditTextLoginUserPw.clearFocus()
        }
    }

    // 로그인 성공 다이얼로그 표시 함수
    private fun showLoginSuccessDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("로그인 성공")
            .setMessage("화면 이동 및 데이터 전달을 구성 필요.")
            .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            .create()
        alertDialog.show()
    }

    // 로그인 실패 다이얼로그 표시 함수
    private fun showLoginFailureDialog(errorMsg:String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("로그인 실패")
            .setMessage(errorMsg)
            .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            .create()
        alertDialog.show()
    }
}