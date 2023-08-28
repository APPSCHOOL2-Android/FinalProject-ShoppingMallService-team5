package com.hifi.hifi_shopping.category.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewTreeObserver
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

        var expanded = false
        fun bind(review: CategoryMainReview) {
            itemReviewCategoryDetailBinding.run {
                //textViewItemReviewCategoryDetailReviewContent.text = review.context
                textViewItemReviewCategoryDetailReviewContent.text = "이것은 샘플 데이터입니다. 이것은 샘플 데이터입니다. 이것은 샘플 데이터입니다. 이것은 샘플 데이터입니다. 이것은 샘플 데이터입니다. 이것은 샘플 데이터입니다. 이것은 샘플 데이터입니다. 이것은 샘플 데이터입니다. 이것은 샘플 데이터입니다. 이것은 샘플 데이터입니다. 이것은 샘플 데이터입니다. 이것은 샘플 데이터입니다."

                textViewItemReviewCategoryDetailReviewContent.post {
                    if (textViewItemReviewCategoryDetailReviewContent.layout.getEllipsisCount(textViewItemReviewCategoryDetailReviewContent.lineCount - 1) > 0) {
                        imageViewItemReviewCategoryDetailExpand.visibility = View.VISIBLE
                        val dpValueHorizontal = 13
                        val dpValueTop = 9
                        val dpValueBottom = 36
                        val density = categoryMainFragment.resources.displayMetrics.density
                        textViewItemReviewCategoryDetailReviewContent.setPadding(
                            (dpValueHorizontal * density).toInt(),
                            (dpValueTop * density).toInt(),
                            (dpValueHorizontal * density).toInt(),
                            (dpValueBottom * density).toInt()
                        )

                        textViewItemReviewCategoryDetailReviewContent.run {
                            setOnClickListener {
                                if (expanded) {
                                    expanded = false
                                    imageViewItemReviewCategoryDetailExpand.setImageResource(R.drawable.expand_more_24px)
                                    maxLines = 4
                                } else {
                                    expanded = true
                                    maxLines = Int.MAX_VALUE
                                    imageViewItemReviewCategoryDetailExpand.setImageResource(R.drawable.expand_less_24px)
                                }
                            }
                        }
                    }
                }

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