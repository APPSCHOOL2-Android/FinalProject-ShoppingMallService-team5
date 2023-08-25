package com.hifi.hifi_shopping.category.ui

import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.category.model.CategoryMainProduct
import com.hifi.hifi_shopping.databinding.ItemProductCategoryDetailBinding

class ProductListAdapter(val callback: (String) -> Unit): ListAdapter<CategoryMainProduct, ProductListAdapter.ProductListViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CategoryMainProduct>() {
            override fun areItemsTheSame(oldItem: CategoryMainProduct, newItem: CategoryMainProduct): Boolean {
                return oldItem.idx == newItem.idx
            }

            override fun areContentsTheSame(oldItem: CategoryMainProduct, newItem: CategoryMainProduct): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val itemProductCategoryDetailBinding = ItemProductCategoryDetailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        itemProductCategoryDetailBinding.root.layoutParams = params

        return ProductListViewHolder(itemProductCategoryDetailBinding)
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ProductListViewHolder(
        val itemProductCategoryDetailBinding: ItemProductCategoryDetailBinding
    ): RecyclerView.ViewHolder(itemProductCategoryDetailBinding.root){
        fun bind(product: CategoryMainProduct) {
            itemProductCategoryDetailBinding.run {
                imageViewItemProductCategoryDetailThumb.setImageResource(R.color.brown2)

                val storage = FirebaseStorage.getInstance()
                val fileRef = storage.reference.child(product.imgSrc)
                fileRef.downloadUrl.addOnCompleteListener {
                    Glide.with(imageViewItemProductCategoryDetailThumb)
                        .load(it.result.toString())
                        .placeholder(R.color.brown2)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                        .into(imageViewItemProductCategoryDetailThumb)
                }

//                Glide.with(imageViewItemProductCategoryDetailThumb)
//                    .load(product.imgSrc)
//                    .placeholder(R.color.brown2)
//                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
//                    .into(imageViewItemProductCategoryDetailThumb)

                textViewItemProductCategoryDetailName.text = product.name
                textViewItemProductCategoryDetailPrice.text = product.price

                root.setOnClickListener {
                    callback(product.idx)
                }
            }
        }
    }
}