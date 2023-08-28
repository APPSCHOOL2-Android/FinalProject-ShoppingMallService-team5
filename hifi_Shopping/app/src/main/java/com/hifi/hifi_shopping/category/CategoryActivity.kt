package com.hifi.hifi_shopping.category

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.auth.vm.AuthViewModel
import com.hifi.hifi_shopping.category.ui.CategoryMainFragment
import com.hifi.hifi_shopping.databinding.ActivityCategoryBinding
import com.hifi.hifi_shopping.rank.RankMainFragment
import com.hifi.hifi_shopping.recommend.RecommendFragment
import com.hifi.hifi_shopping.user.UserActivity
import com.hifi.hifi_shopping.user.model.UserDataClass
import com.hifi.hifi_shopping.user.repository.UserRepository
import com.hifi.hifi_shopping.wish.WishFragment

class CategoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryBinding

    lateinit var categoryViewModel: CategoryViewModel

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var authViewModel: AuthViewModel
    lateinit var userDataClass: UserDataClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this@CategoryActivity)[AuthViewModel::class.java]

        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentCategory) as NavHostFragment
        navController = navHostFragment.navController

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.id == R.id.categoryMainFragment && !(categoryViewModel.searchSubCategory.value ?: false)){
                    finish()
                    return
                }

                navController.popBackStack()

                categoryViewModel.setNavControllerDestination(navController.currentDestination?.id ?: R.id.categoryMainFragment)
            }
        })

        binding.run {
            bottomNavigationViewCategory.run {

                categoryViewModel.run {
                    navControllerDestination.observe(this@CategoryActivity) { destination ->
                        when (destination) {
                            R.id.rankMainFragment -> {
                                menu.forEach {
                                    if (it.itemId == R.id.bottomMenuItemRankMain) {
                                        it.isChecked = true
                                    }
                                }
                            }
                            R.id.recommendFragment -> {
                                menu.forEach {
                                    if (it.itemId == R.id.bottomMenuItemRecommend) {
                                        it.isChecked = true
                                    }
                                }
                            }
                            R.id.categoryMainFragment -> {
                                menu.forEach {
                                    if (it.itemId == R.id.bottomMenuItemCategoryMain) {
                                        it.isChecked = true
                                    }
                                }
                            }
                            R.id.wishFragment -> {
                                menu.forEach {
                                    if (it.itemId == R.id.bottomMenuItemWish) {
                                        it.isChecked = true
                                    }
                                }
                            }
                        }
                    }
                }

                setOnItemSelectedListener {
                    when (it.itemId) {
                        R.id.bottomMenuItemRankMain -> {
                            navController.navigate(R.id.actionToRankMainFragment)
                            categoryViewModel.setSearchSubCategory(false)
                        }
                        R.id.bottomMenuItemRecommend -> {
                            navController.navigate(R.id.actionToRecommendFragment)
                            categoryViewModel.setSearchSubCategory(false)
                        }
                        R.id.bottomMenuItemCategoryMain -> {
                            categoryViewModel.setShowProductOrReview(ContentType.PRODUCT)
                            navController.navigate(R.id.actionToCategoryMainFragment)
                            categoryViewModel.setSearchSubCategory(false)
                        }
                        R.id.bottomMenuItemMyPage -> {
                            val intent = Intent(this@CategoryActivity, UserActivity::class.java)
                            startActivity(intent)
                        }
                        R.id.bottomMenuItemWish -> {
                            navController.navigate(R.id.actionToWishFragment)
                            categoryViewModel.setSearchSubCategory(false)
                        }
                    }
                    true
                }
            }

            categoryViewModel.run {
                searchSubCategory.observe(this@CategoryActivity) {
                    if (!it) {
                        bottomNavigationViewCategory.itemIconTintList =
                            getColorStateList(R.color.navigation_bar_item_tint)
                        bottomNavigationViewCategory.itemTextColor =
                            getColorStateList(R.color.navigation_bar_item_tint)
                    } else {
                        val colorList = ColorStateList(
                            arrayOf(
                                intArrayOf()
                            ),
                            intArrayOf(
                                ContextCompat.getColor(this@CategoryActivity, R.color.brown2)
                            )
                        )

                        val bottomNav = binding.bottomNavigationViewCategory
                        bottomNav.itemIconTintList = colorList
                        bottomNav.itemTextColor = colorList
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        categoryViewModel.setNavControllerDestination(navController.currentDestination?.id ?: R.id.categoryMainFragment)
    }
}