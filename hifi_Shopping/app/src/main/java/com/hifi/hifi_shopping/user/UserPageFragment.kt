package com.hifi.hifi_shopping.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hifi.hifi_shopping.MainActivity
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentMyPageBinding
import com.hifi.hifi_shopping.databinding.FragmentUserPageBinding
import com.hifi.hifi_shopping.user.adapter.UserPageViewPagerAdapter


class UserPageFragment : Fragment() {

    lateinit var fragmentUserPageBinding: FragmentUserPageBinding
    lateinit var mainActivity: MainActivity

    private val tabTextList = listOf("아이템","리뷰")

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: UserPageViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUserPageBinding = FragmentUserPageBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentUserPageBinding.run {
            tabLayout = userPageTabLayout
            viewPager = userPageViewPager

            // adapter 준비 및 연결
            adapter = UserPageViewPagerAdapter(this@UserPageFragment)
            viewPager.adapter = adapter

        }

        // TabLayout, ViewPager 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = tabTextList[0]
                else -> tab.text = tabTextList[1]
            }
        }.attach()

        return fragmentUserPageBinding.root
    }

}