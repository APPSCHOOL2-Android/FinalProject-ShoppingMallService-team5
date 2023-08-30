package com.hifi.hifi_shopping.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hifi.hifi_shopping.databinding.ItemSearchInitRecentSearchBinding
import com.hifi.hifi_shopping.search.SearchViewModel

class RecentSearchListAdapter(
    val userIdx: String,
    val searchViewModel: SearchViewModel,
    val callback: (String) -> Unit
): ListAdapter<String, RecentSearchListAdapter.RecentSearchListViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchListViewHolder {
        val itemSearchInitRecentSearchBinding = ItemSearchInitRecentSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        itemSearchInitRecentSearchBinding.root.layoutParams = params

        return RecentSearchListViewHolder(itemSearchInitRecentSearchBinding)
    }

    override fun onBindViewHolder(holder: RecentSearchListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class RecentSearchListViewHolder(
        val itemSearchInitRecentSearchBinding: ItemSearchInitRecentSearchBinding
    ): RecyclerView.ViewHolder(itemSearchInitRecentSearchBinding.root) {
        fun bind(word: String) {
            itemSearchInitRecentSearchBinding.run {
                root.setOnClickListener {
                    callback(word)
                }

                textViewItemSearchInitRecentSearchWord.text = word

                imageViewRecentSearchWordRemove.setOnClickListener {
                    searchViewModel.removeRecentSearchWord(word, userIdx)
                }
            }
        }
    }
}