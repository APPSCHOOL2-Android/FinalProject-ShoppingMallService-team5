package com.hifi.hifi_shopping.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ItemSearchResultProductBinding
import com.hifi.hifi_shopping.search.SearchViewModel
import com.hifi.hifi_shopping.search.model.SearchProduct

class SearchResultListAdapter(
    val searchViewModel: SearchViewModel,
    val callback: (String) -> Unit
): ListAdapter<SearchProduct, SearchResultListAdapter.SearchResultListViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SearchProduct>() {
            override fun areItemsTheSame(oldItem: SearchProduct, newItem: SearchProduct): Boolean {
                return oldItem.idx == newItem.idx
            }

            override fun areContentsTheSame(oldItem: SearchProduct, newItem: SearchProduct): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultListViewHolder {
        val itemSearchResultProductBinding = ItemSearchResultProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        itemSearchResultProductBinding.root.layoutParams = params

        return SearchResultListViewHolder(itemSearchResultProductBinding)
    }

    override fun onBindViewHolder(holder: SearchResultListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class SearchResultListViewHolder(
        val itemSearchResultProductBinding: ItemSearchResultProductBinding
    ): RecyclerView.ViewHolder(itemSearchResultProductBinding.root) {
        fun bind(product: SearchProduct) {
            itemSearchResultProductBinding.run {
                root.setOnClickListener {
                    callback(product.idx)
                }

                imageViewItemSearchResultProduct.setImageResource(R.drawable.product_sample)

                textViewItemSearchResultProductName.text = product.name

                searchViewModel.getProductImg(product.idx) {
                    Glide.with(imageViewItemSearchResultProduct)
                        .load(it)
                        .into(imageViewItemSearchResultProduct)
                }
            }
        }
    }
}