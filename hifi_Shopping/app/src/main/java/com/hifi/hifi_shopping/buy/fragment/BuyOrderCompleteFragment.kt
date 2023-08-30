package com.hifi.hifi_shopping.buy.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.databinding.FragmentBuyOrderCompleteBinding

class BuyOrderCompleteFragment : Fragment() {

    lateinit var fragmentBuyOrderCompleteBinding: FragmentBuyOrderCompleteBinding
    lateinit var buyActivity: BuyActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentBuyOrderCompleteBinding = FragmentBuyOrderCompleteBinding.inflate(layoutInflater)
        buyActivity = activity as BuyActivity

        fragmentBuyOrderCompleteBinding.run{
            buttonBuyOrderCompleteNext.run{
                setOnClickListener {
                    buyActivity.replaceFragment(BuyActivity.BUY_ORDER_COMPLETELIST_FRAGMENT, false, null)
                }
            }
        }
        return fragmentBuyOrderCompleteBinding.root
    }
}