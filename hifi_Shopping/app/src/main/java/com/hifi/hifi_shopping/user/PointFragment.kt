package com.hifi.hifi_shopping.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentPointBinding

class PointFragment : Fragment() {
    lateinit var fragmentPointBinding: FragmentPointBinding
    lateinit var userActivity: UserActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPointBinding = FragmentPointBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity
        return fragmentPointBinding.root
    }

}