package com.hifi.hifi_shopping.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hifi.hifi_shopping.databinding.FragmentAuthFindPwBinding

class AuthFindPwFragment : Fragment() {

    lateinit var fragmentAuthFindPwBinding: FragmentAuthFindPwBinding
    lateinit var authActivity: AuthActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthFindPwBinding = FragmentAuthFindPwBinding.inflate(inflater)
        authActivity = activity as AuthActivity

        fragmentAuthFindPwBinding.run {
            // 네비게이션 아이콘을 클릭했을 때의 동작 설정
            toolbarAuthFindPw.setNavigationOnClickListener {
                authActivity.removeFragment(AuthActivity.AUTH_FIND_PW_FRAGMENT)
            }

            // "다음" 버튼 클릭 시
            buttonFindPwCheck.run{
                setOnClickListener{
                    authActivity.replaceFragment(AuthActivity.AUTH_FIND_RESULT_FRAGMENT, true, null)
                }
            }
        }

        return   fragmentAuthFindPwBinding.root
    }


}