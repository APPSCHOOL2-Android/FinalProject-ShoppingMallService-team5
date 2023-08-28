package com.hifi.hifi_shopping.rank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.hifi.hifi_shopping.databinding.FragmentRankMainBinding

class RankMainFragment : Fragment() {

    lateinit var fragmentRankMainBinding : FragmentRankMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentRankMainBinding = FragmentRankMainBinding.inflate(inflater, container, false)

        val viewPager = fragmentRankMainBinding.ViewPagerRankMain
        val tabLayout = fragmentRankMainBinding.TabLayoutRankMain

        // 어댑터 연결
        val adapter = PagerAdapter(childFragmentManager, lifecycle)
        adapter.addFragment(RankItemFragment())
        adapter.addFragment(RankSubsFragment())

        viewPager.adapter = adapter

        // Tab레이아웃 title 구성
        val tabTitles = listOf("종류 별", "구독")

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        return fragmentRankMainBinding.root
    }
}

class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentList = mutableListOf<Fragment>()

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }
    override fun getItemCount(): Int {
        return fragmentList.size
    }
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}
