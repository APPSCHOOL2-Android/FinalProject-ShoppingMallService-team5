package com.hifi.hifi_shopping.rank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentRankItemBinding
import com.hifi.hifi_shopping.databinding.FragmentRankSubsBinding

class RankSubsFragment : Fragment() {

    lateinit var fragmentRankSubsBinding: FragmentRankSubsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentRankSubsBinding = FragmentRankSubsBinding.inflate(layoutInflater)

        return fragmentRankSubsBinding.root
    }
}