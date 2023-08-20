package com.hifi.hifi_shopping_sales.seller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hifi.hifi_shopping_sales.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var sellerActivity: SellerActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater)
        sellerActivity = activity as SellerActivity
        fragmentLoginBinding.run{
            buttonLogin.setOnClickListener {
                sellerActivity.replaceFragment(SellerActivity.ITEM_LIST_FRAGMENT, true, null)
            }
        }
        return fragmentLoginBinding.root
    }
}