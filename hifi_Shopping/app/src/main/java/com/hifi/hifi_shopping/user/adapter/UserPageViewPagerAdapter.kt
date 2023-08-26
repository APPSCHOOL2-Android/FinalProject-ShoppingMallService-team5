package com.hifi.hifi_shopping.user.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hifi.hifi_shopping.user.UserPageFragment
import com.hifi.hifi_shopping.user.UserPageItemFragment
import com.hifi.hifi_shopping.user.UserPageReviewFragment

class UserPageViewPagerAdapter(fragment: UserPageFragment) : FragmentStateAdapter(fragment){
    var fragments = arrayOf(UserPageItemFragment(), UserPageReviewFragment())

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }
}