package com.hifi.hifi_shopping.wish

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.databinding.FragmentWishBinding
import com.hifi.hifi_shopping.rank.RankActivity
import com.hifi.hifi_shopping.recommend.RecommendActivity
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.user.UserActivity

class WishFragment : Fragment() {
    lateinit var fragmentWishBinding : FragmentWishBinding
    lateinit var categoryActivity: CategoryActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentWishBinding = FragmentWishBinding.inflate(layoutInflater)
        categoryActivity = activity as CategoryActivity

        fragmentWishBinding.run {
            wishToolbar.run {
                setNavigationOnClickListener {

                }
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_item_search -> {
                            val intent = Intent(categoryActivity, SearchActivity::class.java)
                            startActivity(intent)
                        }
                        R.id.menu_item_cart -> {
                            val intent = Intent(categoryActivity, UserActivity::class.java)
                            intent.putExtra("whereFrom","wish")
                            intent.putExtra("userFragmentType","cart")
                            intent.putExtra("navigateTo",R.id.bottomMenuItemRankMain)
                            intent.putExtra("userEmail", categoryActivity.userDataClass.email)
                            intent.putExtra("userIdx", categoryActivity.userDataClass.idx)
                            intent.putExtra("userNickname", categoryActivity.userDataClass.nickname)
                            intent.putExtra("userPw", categoryActivity.userDataClass.pw)
                            intent.putExtra("userVerify", categoryActivity.userDataClass.verify)
                            intent.putExtra("userPhoneNum", categoryActivity.userDataClass.phoneNum)
                            intent.putExtra("userProfileImg", categoryActivity.userDataClass.profileImg)
                            startActivity(intent)
//                            userActivity.replaceFragment(UserActivity.CART_FRAGMENT, true, null)
                        }
                    }
                    true
                }
            }
        }

        return fragmentWishBinding.root
    }
}