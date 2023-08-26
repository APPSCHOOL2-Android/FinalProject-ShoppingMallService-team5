package com.hifi.hifi_shopping.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentCartBinding

class CartFragment : Fragment() {
   lateinit var fragmentCartBinding: FragmentCartBinding
   lateinit var userActivity: UserActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentCartBinding = FragmentCartBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity
        return fragmentCartBinding.root
    }


}