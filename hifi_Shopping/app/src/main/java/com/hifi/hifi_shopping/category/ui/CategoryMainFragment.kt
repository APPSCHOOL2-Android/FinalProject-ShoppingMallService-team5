package com.hifi.hifi_shopping.category.ui

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.hifi.hifi_shopping.MainActivity
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.databinding.FragmentCategoryMainBinding

class CategoryMainFragment : Fragment() {

    lateinit var categoryActivity: CategoryActivity
    lateinit var binding: FragmentCategoryMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoryActivity = activity as CategoryActivity
        binding = FragmentCategoryMainBinding.inflate(layoutInflater)

        val colorList = ColorStateList(
            arrayOf(
                intArrayOf()
            ),
            intArrayOf(
                ContextCompat.getColor(categoryActivity, R.color.brown)
            )
        )

        val bottomNav = categoryActivity.binding.bottomNavigationViewCategory

        binding.run {
            groupCategoryMainPrepare.setOnClickListener {
                findNavController().navigate(R.id.action_categoryMainFragment_to_categoryDetailFragment)
                bottomNav.itemIconTintList = colorList
                bottomNav.itemTextColor = colorList
            }

            groupCategoryMainManage.setOnClickListener {
                findNavController().navigate(R.id.action_categoryMainFragment_to_categoryDetailFragment)
                bottomNav.itemIconTintList = colorList
                bottomNav.itemTextColor = colorList
            }

            groupCategoryMainHobby.setOnClickListener {
                findNavController().navigate(R.id.action_categoryMainFragment_to_categoryDetailFragment)
                bottomNav.itemIconTintList = colorList
                bottomNav.itemTextColor = colorList
            }

            groupCategoryMainCouple.setOnClickListener {
                findNavController().navigate(R.id.action_categoryMainFragment_to_categoryDetailFragment)
                bottomNav.itemIconTintList = colorList
                bottomNav.itemTextColor = colorList
            }
        }

        return binding.root
    }
}