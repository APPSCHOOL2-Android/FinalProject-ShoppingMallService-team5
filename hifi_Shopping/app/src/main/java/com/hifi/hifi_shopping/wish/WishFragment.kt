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
import com.hifi.hifi_shopping.user.UserActivity

class WishFragment : Fragment() {
    lateinit var fragmentWishBinding : FragmentWishBinding
    lateinit var wishActivity: WishActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentWishBinding = FragmentWishBinding.inflate(layoutInflater)
        wishActivity = activity as WishActivity

        fragmentWishBinding.run {
            wishBottomNavigationView.run {
                selectedItemId = R.id.bottomMenuItemWish
                setOnItemSelectedListener {
                    when (it.itemId) {
                        R.id.bottomMenuItemRankMain ->{
                            val intent = Intent(wishActivity, RankActivity::class.java)
                            startActivity(intent)

                        }
                        R.id.bottomMenuItemCategoryMain->{
                            val intent = Intent(wishActivity, CategoryActivity::class.java)
                            startActivity(intent)

                        }
                        R.id.bottomMenuItemRecommend ->{
                            val intent = Intent(wishActivity, RecommendActivity::class.java)
                            startActivity(intent)
                        }
                        R.id.bottomMenuItemMyPage ->{
                            val intent = Intent(wishActivity, UserActivity::class.java)
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