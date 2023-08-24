package com.hifi.hifi_shopping.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentEditUserBinding
import com.hifi.hifi_shopping.search.SearchActivity

class EditUserFragment : Fragment() {

    lateinit var fragmentEditUserBinding: FragmentEditUserBinding
    lateinit var userActivity : UserActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentEditUserBinding = FragmentEditUserBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity

        fragmentEditUserBinding.run {
            editUserToolbar.run {
                setNavigationOnClickListener {
                    userActivity.removeFragment(UserActivity.MY_PAGE_FRAGMENT)

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

        }
        return fragmentEditUserBinding.root
    }
}