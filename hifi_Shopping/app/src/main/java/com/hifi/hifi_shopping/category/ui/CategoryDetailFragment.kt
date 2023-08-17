package com.hifi.hifi_shopping.category.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentCategoryDetailBinding
import com.hifi.hifi_shopping.databinding.ItemProductCategoryDetailBinding
import java.lang.Math.abs

class CategoryDetailFragment : Fragment() {

    lateinit var binding: FragmentCategoryDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryDetailBinding.inflate(layoutInflater)

        binding.run {
            recyclerViewCategoryDetail.run {
                adapter = ProductListAdapter()
                layoutManager = LinearLayoutManager(context)
            }

            appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->

                val seekPosition = abs(verticalOffset) / appBarLayout.totalScrollRange.toFloat()
                binding.motionLayout.progress = seekPosition
            }
        }

        return binding.root
    }

    inner class ProductListAdapter: RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val itemBinding = ItemProductCategoryDetailBinding.inflate(layoutInflater)

            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            itemBinding.root.layoutParams = params

            return ProductViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            holder.bind()
        }

        override fun getItemCount(): Int {
            return 20
        }

        inner class ProductViewHolder(val itemBinding: ItemProductCategoryDetailBinding): RecyclerView.ViewHolder(itemBinding.root) {
            fun bind() {
                itemBinding.textViewItemProductCategoryDetailName.text = "제품명$adapterPosition"

                Glide.with(itemBinding.imageViewItemProductCategoryDetail)
                    .load("https://picsum.photos/${120+adapterPosition}/${120+adapterPosition}")
                    .into(itemBinding.imageViewItemProductCategoryDetail)
            }
        }
    }
}