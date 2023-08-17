package com.hifi.hifi_shopping.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hifi.hifi_shopping.MainActivity
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentAuthLoginBinding

class AuthLoginFragment : Fragment() {

    lateinit var fragmentAuthLoginBinding: FragmentAuthLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthLoginBinding = FragmentAuthLoginBinding.inflate(inflater)

        // MaterialCheckBox 클릭 리스너 등록
        fragmentAuthLoginBinding.CheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            // isChecked 값이 true일 경우, 체크가 되었을 때 실행되는 코드
            if (isChecked) {
                // 체크가 되었을 때 처리할 코드 작성
                fragmentAuthLoginBinding.textViewTest.text = "체크되었습니다."
            } else {
                // 체크가 해제되었을 때 처리할 코드 작성
                fragmentAuthLoginBinding.textViewTest.text = "체크가 해제되었습니다."
            }
        }

        return fragmentAuthLoginBinding.root
    }
}