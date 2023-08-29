package com.hifi.hifi_shopping.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hifi.hifi_shopping.MainActivity
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentMyPageBinding
import com.hifi.hifi_shopping.databinding.FragmentUserPageBinding
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.user.adapter.UserPageViewPagerAdapter
import com.hifi.hifi_shopping.user.model.UserDataClass
import com.hifi.hifi_shopping.user.vm.OrderViewModel
import com.hifi.hifi_shopping.user.vm.ReviewViewModel
import com.hifi.hifi_shopping.user.vm.SubscribeViewModel
import java.net.UnknownServiceException


class UserPageFragment : Fragment() {

    lateinit var fragmentUserPageBinding: FragmentUserPageBinding
    lateinit var userActivity: UserActivity

    private val tabTextList = listOf("아이템","리뷰")

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: UserPageViewPagerAdapter

    lateinit var reviewViewModel : ReviewViewModel
    lateinit var subscribeViewModel: SubscribeViewModel
    lateinit var orderViewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUserPageBinding = FragmentUserPageBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity

        reviewViewModel = ViewModelProvider(userActivity)[ReviewViewModel::class.java]
        subscribeViewModel = ViewModelProvider(userActivity)[SubscribeViewModel::class.java]
        orderViewModel = ViewModelProvider(userActivity)[OrderViewModel::class.java]

        val userLogin = userActivity.userTemp
        val userPagesUser= userActivity.userTemp
//        val userPagesUser = UserDataClass("2", "alohalo98@naver.com", "1222", "테스트계정", "false", "010-2222-2222", "")

        reviewViewModel.run {
            getReviewListByUser(userPagesUser.idx)
            reviewDataList.observe(userActivity){
                val reviewCount = reviewDataList.value?.size
                fragmentUserPageBinding.userPageReviewCount.text = reviewCount.toString()
            }
        }
        subscribeViewModel.run {
            followerList.observe(userActivity){
                val followerCount = followerList.value?.size
                fragmentUserPageBinding.userPageFollowerCount.text = followerCount.toString()
            }
        }

        orderViewModel.run {
            getOrderListByUser(userPagesUser.idx)
            productDataList.observe(userActivity){
                val orderItemsCount = productDataList.value?.size
                fragmentUserPageBinding.userPageItemCount.text = orderItemsCount.toString()

            }
        }


        fragmentUserPageBinding.run {
            userPageToolbar.run {
                title = userPagesUser.nickname
                setNavigationOnClickListener{
                    userActivity.whatIsPrev(UserActivity.USER_PAGE_FRAGMENT)
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


            tabLayout = userPageTabLayout
            viewPager = userPageViewPager

            // adapter 준비 및 연결
            adapter = UserPageViewPagerAdapter(this@UserPageFragment)
            viewPager.adapter = adapter

            userPageProfile.run {
                userActivity.getUserProfileImg(userPagesUser,this)
            }

            userPageUserNick.text = userPagesUser.nickname

            if(userLogin==userPagesUser){
                userPageBtnSubscribe.visibility = View.INVISIBLE
            }


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