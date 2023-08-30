package com.hifi.hifi_shopping.category

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.databinding.ActivityCategoryBinding
import com.hifi.hifi_shopping.user.UserActivity
import com.hifi.hifi_shopping.user.model.UserDataClass


class CategoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryBinding

    lateinit var categoryViewModel: CategoryViewModel

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var userDataClass: UserDataClass

    var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedIntent = intent
        if (receivedIntent != null && receivedIntent.hasExtra("userEmail")) {
            val email = receivedIntent.getStringExtra("userEmail")!!
            val userIdx = receivedIntent.getStringExtra("userIdx")!!
            val userNickname = receivedIntent.getStringExtra("userNickname")!!
            val userPw = receivedIntent.getStringExtra("userPw")!!
            val userProfileImg = receivedIntent.getStringExtra("userProfileImg")!!
            val userVerify = receivedIntent.getStringExtra("userVerify")!!
            val userPhoneNum = receivedIntent.getStringExtra("userPhoneNum")!!
            val newUserData = UserDataClass(userIdx, email, userPw, userNickname,
                userVerify, userPhoneNum, userProfileImg)

            userDataClass = newUserData
        }

        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        categoryViewModel.currentUserIdx = userDataClass.idx
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentCategory) as NavHostFragment
        navController = navHostFragment.navController

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.id == R.id.categoryMainFragment && !(categoryViewModel.searchSubCategory.value ?: false)){
                    if (doubleBackToExitPressedOnce) {
                        finish()
                        return
                    }

                    this@CategoryActivity.doubleBackToExitPressedOnce = true
                    Snackbar.make(binding.root, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Snackbar.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        doubleBackToExitPressedOnce = false
                    }, 2000)
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
                        Log.d("brudenell", "change")
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
                    navigateToFragment(it.itemId, false)
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

    fun navigateToFragment(navigateTo: Int, fromMyPage: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean("fromMyPage", fromMyPage)

        when (navigateTo) {
            R.id.bottomMenuItemRankMain -> {
                navController.navigate(R.id.actionToRankMainFragment, bundle)
                categoryViewModel.setSearchSubCategory(false)
            }
            R.id.bottomMenuItemRecommend -> {
                navController.navigate(R.id.actionToRecommendFragment, bundle)
                categoryViewModel.setSearchSubCategory(false)
            }
            R.id.bottomMenuItemCategoryMain -> {
                categoryViewModel.setShowProductOrReview(ContentType.PRODUCT)
                navController.navigate(R.id.actionToCategoryMainFragment)
                categoryViewModel.setSearchSubCategory(false)
            }
            R.id.bottomMenuItemMyPage -> {
                val intent = Intent(this@CategoryActivity, UserActivity::class.java)
                intent.putExtra("userEmail", userDataClass.email)
                intent.putExtra("userIdx", userDataClass.idx)
                intent.putExtra("userNickname", userDataClass.nickname)
                intent.putExtra("userPw", userDataClass.pw)
                intent.putExtra("userVerify", userDataClass.verify)
                intent.putExtra("userPhoneNum", userDataClass.phoneNum)
                intent.putExtra("userProfileImg", userDataClass.profileImg)
                intent.putExtra("whereFrom", "category")
                startActivity(intent)
            }
            R.id.bottomMenuItemWish -> {
                navController.navigate(R.id.actionToWishFragment, bundle)
                categoryViewModel.setSearchSubCategory(false)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val navigateTo = intent?.getIntExtra("navigateTo", R.id.bottomMenuItemCategoryMain) ?: R.id.bottomMenuItemCategoryMain

        var fromMyPage = false
        if (navigateTo != R.id.bottomMenuItemCategoryMain) {
            fromMyPage = true
        }

        navigateToFragment(navigateTo, fromMyPage)
    }

    override fun onResume() {
        super.onResume()

        categoryViewModel.setNavControllerDestination(navController.currentDestination?.id ?: R.id.categoryMainFragment)
    }

    fun sendIntentProcuctUserInfo(productidx: String){
        val intent = Intent(this, BuyActivity::class.java)
        intent.putExtra("productIdx", productidx)
        intent.putExtra("userEmail", userDataClass.email)
        intent.putExtra("userIdx", userDataClass.idx)
        intent.putExtra("userNickname", userDataClass.nickname)
        intent.putExtra("userPw", userDataClass.pw)
        intent.putExtra("userVerify", userDataClass.verify)
        intent.putExtra("userPhoneNum", userDataClass.phoneNum)
        intent.putExtra("userProfileImg", userDataClass.profileImg)
        startActivity(intent)
    }
}