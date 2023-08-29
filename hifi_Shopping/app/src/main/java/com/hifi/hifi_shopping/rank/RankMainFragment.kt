package com.hifi.hifi_shopping.rank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.databinding.FragmentRankMainBinding
import com.hifi.hifi_shopping.user.UserActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.timer

class RankMainFragment : Fragment() {

    lateinit var fragmentRankMainBinding : FragmentRankMainBinding
    lateinit var categoryActivity: CategoryActivity

    var fromMyPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        fragmentRankMainBinding = FragmentRankMainBinding.inflate(inflater, container, false)
        categoryActivity = activity as CategoryActivity

        fromMyPage = arguments?.getBoolean("fromMyPage") ?: false

        if (fromMyPage) {
            categoryActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        categoryActivity.navController.popBackStack()
                        categoryActivity.categoryViewModel.setNavControllerDestination(categoryActivity.navController.currentDestination?.id ?: R.id.categoryMainFragment)
                    }

                    if (fromMyPage) {
                        val intent = Intent(categoryActivity, UserActivity::class.java)
                        intent.putExtra("userEmail", categoryActivity.userDataClass.email)
                        intent.putExtra("userIdx", categoryActivity.userDataClass.idx)
                        intent.putExtra("userNickname", categoryActivity.userDataClass.nickname)
                        intent.putExtra("userPw", categoryActivity.userDataClass.pw)
                        intent.putExtra("userVerify", categoryActivity.userDataClass.verify)
                        intent.putExtra("userPhoneNum", categoryActivity.userDataClass.phoneNum)
                        intent.putExtra("userProfileImg", categoryActivity.userDataClass.profileImg)
                        intent.putExtra("whereFrom", "category")
                        startActivity(intent)
                    }
                }
            })
        }

        fragmentRankMainBinding.run {
            toolbarRankMain.run {
                setNavigationOnClickListener {
                    categoryActivity.onBackPressedDispatcher.onBackPressed()
                }
            }
        }

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
