package com.hifi.hifi_shopping.category.ui

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.category.model.CategoryMainReview
import com.hifi.hifi_shopping.databinding.ItemProductCategoryDetailBinding
import com.hifi.hifi_shopping.databinding.ItemReviewCategoryDetailBinding

class ReviewListAdapter(
    val categoryMainFragment: CategoryMainFragment,
    val categoryMainViewModel: CategoryMainViewModel,
    val profileClickCallback: () -> Unit,
    val productClickCallback: (String) -> Unit
): ListAdapter<CategoryMainReview, ReviewListAdapter.ReviewListViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CategoryMainReview>() {
            override fun areItemsTheSame(oldItem: CategoryMainReview, newItem: CategoryMainReview): Boolean {
                return oldItem.idx == newItem.idx
            }

            override fun areContentsTheSame(oldItem: CategoryMainReview, newItem: CategoryMainReview): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewListViewHolder {
        val itemReviewCategoryDetailBinding = ItemReviewCategoryDetailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        itemReviewCategoryDetailBinding.root.layoutParams = params

        return ReviewListViewHolder(itemReviewCategoryDetailBinding)
    }

    override fun onBindViewHolder(holder: ReviewListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ReviewListViewHolder(
        val itemReviewCategoryDetailBinding: ItemReviewCategoryDetailBinding
    ): RecyclerView.ViewHolder(itemReviewCategoryDetailBinding.root) {
        fun bind(review: CategoryMainReview) {
            itemReviewCategoryDetailBinding.run {
                textViewItemReviewCategoryDetailReviewContent.text = review.context
                textViewItemReviewCategoryDetailLikeCount.text = review.likeCnt

                categoryMainViewModel.getUser(review.writerIdx) { user ->
                    textViewItemReviewCategoryDetailUserName.text = user.nickname

                    Glide.with(imageViewItemReviewCategoryDetailUserThumb)
                        .load(user.profileImg)
                        .placeholder(R.color.white)
                        .circleCrop()
                        .into(imageViewItemReviewCategoryDetailUserThumb)
                }

                categoryMainViewModel.getUserFollowerCnt(review.writerIdx) { followerCnt, subscribing ->
                    updateSubscribeData(review.writerIdx, followerCnt, subscribing)
                }

                buttonItemReviewCategoryDetailSubscribe.setOnClickListener {
                    if (buttonItemReviewCategoryDetailSubscribe.text == "구독") {
                        categoryMainViewModel.setSubscribe(review.writerIdx, true) {
                            categoryMainViewModel.getUserFollowerCnt(review.writerIdx) { followerCnt, subscribing ->
                                updateSubscribeData(review.writerIdx, followerCnt, subscribing)
                            }
                        }
                    } else {
                        categoryMainViewModel.setSubscribe(review.writerIdx, false) {
                            categoryMainViewModel.getUserFollowerCnt(review.writerIdx) { followerCnt, subscribing ->
                                updateSubscribeData(review.writerIdx, followerCnt, subscribing)
                            }
                        }
                    }
                }

                categoryMainViewModel.getUserItemCnt(review.writerIdx) {
                    textViewItemReviewCategoryDetailItemCount.text = it.toString()
                }

                categoryMainViewModel.getUserReviewCnt(review.writerIdx) {
                    textViewItemReviewCategoryDetailReviewCount.text = it.toString()
                }

                categoryMainViewModel.getReviewProductInfo(review.productIdx) {
                    textViewItemReviewCategoryDetailProductName.text = it.name
                    textViewItemReviewCategoryDetailProductPrice.text = it.price

                    Glide.with(imageViewItemReviewCategoryDetailProductThumb)
                        .load(it.imgSrc)
                        .placeholder(R.color.white)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                        .into(imageViewItemReviewCategoryDetailProductThumb)
                }

                layerItemReviewCategoryDetailUserInfo.setOnClickListener {
                    profileClickCallback()
                }

                cardViewItemReviewCategoryDetailProduct.setOnClickListener {
                    productClickCallback(review.productIdx)
                }
            }
        }

        fun updateSubscribeData(writerIdx: String, followerCnt: Int, subscribing: Boolean) {
            itemReviewCategoryDetailBinding.run {
                if (subscribing) {
                    buttonItemReviewCategoryDetailSubscribe.text = "구독 취소"
                    buttonItemReviewCategoryDetailSubscribe.setBackgroundResource(R.drawable.background_subscribe_button_subscribing)
                } else {
                    buttonItemReviewCategoryDetailSubscribe.text = "구독"
                    buttonItemReviewCategoryDetailSubscribe.setBackgroundResource(R.drawable.background_subscribe_button_not_subscribing)
                }

                if (categoryMainViewModel.currentUserIdx != writerIdx)
                    buttonItemReviewCategoryDetailSubscribe.visibility = View.VISIBLE
                else
                    buttonItemReviewCategoryDetailSubscribe.visibility = View.INVISIBLE

                textViewItemReviewCategoryDetailFollowerCount.text = followerCnt.toString()
            }
        }
    }
}