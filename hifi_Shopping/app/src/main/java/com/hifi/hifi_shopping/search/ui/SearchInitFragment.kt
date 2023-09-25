package com.hifi.hifi_shopping.search.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentSearchInitBinding
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.search.SearchViewModel
import com.hifi.hifi_shopping.search.adapter.RecentSearchListAdapter

class SearchInitFragment : Fragment() {

    lateinit var fragmentSearchInitBinding: FragmentSearchInitBinding
    lateinit var searchActivity: SearchActivity

    lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchActivity = activity as SearchActivity
        fragmentSearchInitBinding = FragmentSearchInitBinding.inflate(layoutInflater)

        searchViewModel = ViewModelProvider(searchActivity)[SearchViewModel::class.java]

        val recentSearchListAdapter = RecentSearchListAdapter(searchActivity.userDataClass.idx, searchViewModel) {
            searchActivity.actionSearchRecentWord(it)
        }

        searchViewModel.getRecentSearchWord(searchActivity.userDataClass.idx)

        searchViewModel.recentSearchWordList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                fragmentSearchInitBinding.recyclerViewSearchInitRecentSearch.alpha = 1f
                fragmentSearchInitBinding.textViewNoSearchRecord.visibility = View.INVISIBLE
            } else {
                fragmentSearchInitBinding.recyclerViewSearchInitRecentSearch.alpha = 0f
                fragmentSearchInitBinding.textViewNoSearchRecord.visibility = View.VISIBLE
            }
            recentSearchListAdapter.submitList(it)
        }

        fragmentSearchInitBinding.run {
            recyclerViewSearchInitRecentSearch.run {
                adapter = recentSearchListAdapter
                layoutManager = LinearLayoutManager(searchActivity)
                addItemDecoration(DividerItemDecoration(searchActivity, DividerItemDecoration.VERTICAL))
            }
        }

        return fragmentSearchInitBinding.root
    }
}