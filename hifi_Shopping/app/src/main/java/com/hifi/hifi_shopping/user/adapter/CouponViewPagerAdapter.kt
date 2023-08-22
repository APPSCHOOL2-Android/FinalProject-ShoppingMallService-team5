package com.hifi.hifi_shopping.user.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hifi.hifi_shopping.user.CouponDisabledFragment
import com.hifi.hifi_shopping.user.CouponEnabledFragment
import com.hifi.hifi_shopping.user.CouponFragment

class CouponViewPagerAdapter(fragment: CouponFragment) : FragmentStateAdapter(fragment){
    var fragments = arrayOf(CouponEnabledFragment(), CouponDisabledFragment())

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }
}