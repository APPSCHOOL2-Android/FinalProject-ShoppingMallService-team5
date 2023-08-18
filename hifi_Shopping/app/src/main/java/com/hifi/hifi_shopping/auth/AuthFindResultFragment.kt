package com.hifi.hifi_shopping.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hifi.hifi_shopping.databinding.FragmentAuthFindResultBinding


class AuthFindResultFragment : Fragment() {

    lateinit var fragmentAuthFindResultBinding: FragmentAuthFindResultBinding
    lateinit var authActivity: AuthActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAuthFindResultBinding = FragmentAuthFindResultBinding.inflate(inflater)
        authActivity = activity as AuthActivity

        fragmentAuthFindResultBinding.run {
            // 네비게이션 아이콘을 클릭했을 때의 동작 설정
            toolbarAuthFindResult.setNavigationOnClickListener {
                authActivity.onBackPressed()
            }
        }

        return fragmentAuthFindResultBinding.root
    }


}