package com.hifi.hifi_shopping.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentAuthJoinBinding
import com.hifi.hifi_shopping.databinding.FragmentAuthLoginBinding


class AuthJoinFragment : Fragment() {

    lateinit var fragmentAuthJoinBinding: FragmentAuthJoinBinding
    lateinit var authActivity: AuthActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthJoinBinding = FragmentAuthJoinBinding.inflate(inflater)
        authActivity = activity as AuthActivity

        fragmentAuthJoinBinding.run {
            // 네비게이션 아이콘을 클릭했을 때의 동작 설정
            toolbarAuthJoin.setNavigationOnClickListener {
                authActivity.removeFragment(AuthActivity.AUTH_JOIN_FRAGMENT)
            }
        }

        return fragmentAuthJoinBinding.root
    }
}