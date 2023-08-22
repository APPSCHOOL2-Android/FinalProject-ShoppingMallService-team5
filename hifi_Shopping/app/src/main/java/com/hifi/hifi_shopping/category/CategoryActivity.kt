package com.hifi.hifi_shopping.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.AppBarLayout
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentCategory) as NavHostFragment
        val navController = navHostFragment.navController

        binding.run {
            bottomNavigationViewCategory.selectedItemId = R.id.categoryFragment

            bottomNavigationViewCategory.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.categoryFragment -> {
                        bottomNavigationViewCategory.itemIconTintList = getColorStateList(R.color.navigation_bar_item_tint)
                        bottomNavigationViewCategory.itemTextColor = getColorStateList(R.color.navigation_bar_item_tint)
                    }
                }
                return@setOnItemSelectedListener true
            }
        }
    }
}