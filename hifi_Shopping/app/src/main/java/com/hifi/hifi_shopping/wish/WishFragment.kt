package com.hifi.hifi_shopping.wish

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.databinding.FragmentWishBinding
import com.hifi.hifi_shopping.rank.RankActivity
import com.hifi.hifi_shopping.recommend.RecommendActivity
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.user.UserActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WishFragment : Fragment() {
    lateinit var fragmentWishBinding : FragmentWishBinding
    lateinit var categoryActivity: CategoryActivity

    var fromMyPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentWishBinding = FragmentWishBinding.inflate(inflater, container, false)
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

        fragmentWishBinding.run {
            wishToolbar.run {
                setNavigationOnClickListener {
                    categoryActivity.onBackPressedDispatcher.onBackPressed()
                }
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_item_search -> {
                            val intent = Intent(categoryActivity, SearchActivity::class.java)
                            startActivity(intent)
                        }
                        R.id.menu_item_cart -> {
                            val userActivity = activity as UserActivity
                            val intent = Intent(categoryActivity, UserActivity::class.java)
                            startActivity(intent)
                            userActivity.replaceFragment(UserActivity.CART_FRAGMENT, true, null)
                        }
                    }
                    true
                }
            }
            wishBottomNavigationView.run {
                selectedItemId = R.id.bottomMenuItemWish
                setOnItemSelectedListener {
                    when (it.itemId) {
                        R.id.bottomMenuItemRankMain ->{
                            val intent = Intent(categoryActivity, RankActivity::class.java)
                            startActivity(intent)

                        }
                        R.id.bottomMenuItemCategoryMain->{
                            val intent = Intent(categoryActivity, CategoryActivity::class.java)
                            startActivity(intent)

                        }
                        R.id.bottomMenuItemRecommend ->{
                            val intent = Intent(categoryActivity, RecommendActivity::class.java)
                            startActivity(intent)
                        }
                        R.id.bottomMenuItemMyPage ->{
                            val intent = Intent(categoryActivity, UserActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    return@setOnItemSelectedListener true
                }
            }

        }

        return fragmentWishBinding.root
    }
}