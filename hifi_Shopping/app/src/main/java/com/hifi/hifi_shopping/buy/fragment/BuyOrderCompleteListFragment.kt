package com.hifi.hifi_shopping.buy.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.databinding.FragmentBuyOrderCompleteListBinding


class BuyOrderCompleteListFragment : Fragment() {

    lateinit var fragmentBuyOrderCompleteListBinding: FragmentBuyOrderCompleteListBinding
    lateinit var buyActivity: BuyActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentBuyOrderCompleteListBinding = FragmentBuyOrderCompleteListBinding.inflate(layoutInflater)
        buyActivity = activity as BuyActivity

        return fragmentBuyOrderCompleteListBinding.root
    }

}