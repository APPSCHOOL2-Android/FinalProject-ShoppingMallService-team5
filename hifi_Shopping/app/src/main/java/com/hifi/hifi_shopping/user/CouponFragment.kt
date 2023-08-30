package com.hifi.hifi_shopping.user

import android.content.Intent
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
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.databinding.FragmentCouponBinding
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.user.adapter.CouponViewPagerAdapter


class CouponFragment : Fragment() {

    lateinit var fragmentCouponBinding: FragmentCouponBinding
    lateinit var userActivity: UserActivity

    private val tabTextList = listOf("보유 중인 쿠폰", "미보유 중인 쿠폰")

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: CouponViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentCouponBinding = FragmentCouponBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity

        fragmentCouponBinding.run {
            couponToolbar.run {
                setNavigationOnClickListener {
                    userActivity.removeFragment(UserActivity.COUPON_FRAGMENT)
                }

                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_item_search -> {
                            val intent = Intent(userActivity, SearchActivity::class.java)
                            startActivity(intent)
                        }
                        R.id.menu_item_cart -> {
                            userActivity.replaceFragment(UserActivity.CART_FRAGMENT, true, null)
                        }
                    }
                    true
                }


            }
            tabLayout = couponTabLayout
            viewPager = couponViewPager

            // adapter 준비 및 연결
            adapter = CouponViewPagerAdapter(this@CouponFragment)
            viewPager.adapter = adapter

        }

        // TabLayout, ViewPager 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = tabTextList[0]
                else -> tab.text = tabTextList[1]
            }
        }.attach()

        return fragmentCouponBinding.root
    }

}