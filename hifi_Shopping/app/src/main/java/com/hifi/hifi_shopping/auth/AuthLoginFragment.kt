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


        return fragmentAuthLoginBinding.root
    }
}