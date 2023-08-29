package com.hifi.hifi_shopping.subscribe

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivitySubscribeBinding
import com.hifi.hifi_shopping.databinding.SubscribeUserListItemBinding
import com.hifi.hifi_shopping.databinding.SubscribeUserReviewItemBinding
import com.hifi.hifi_shopping.subscribe.vm.ReviewBySubscribeViewModel
import com.hifi.hifi_shopping.subscribe.vm.SubscribeViewModel

class SubscribeActivity : AppCompatActivity() {

    lateinit var activitySubscribeBinding: ActivitySubscribeBinding
    lateinit var subscribeViewModel: SubscribeViewModel
    lateinit var reviewBySubscribeViewModel: ReviewBySubscribeViewModel
    val userIdx = "0"
    val rowSubscribeList = mutableListOf<SubscribeClass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiSetting()
    }
    fun uiSetting(){
        activitySubscribeBinding = ActivitySubscribeBinding.inflate(layoutInflater)
        setContentView(activitySubscribeBinding.root)

        subscribeViewModel = ViewModelProvider(this)[SubscribeViewModel::class.java]
        reviewBySubscribeViewModel = ViewModelProvider(this)[ReviewBySubscribeViewModel::class.java]
        subscribeViewModel.run{
            followerIdx.observe(this@SubscribeActivity){
                subscribeViewModel.getProfileByUserIdx(it)
            }
            subscribe.observe(this@SubscribeActivity){
                rowSubscribeList.add(it)
                reviewBySubscribeViewModel.getReviewByWriterIdx(it.followerIdx, it.nickname, it.profile)
                activitySubscribeBinding.subscribeUserRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
        reviewBySubscribeViewModel.run{
        }

        activitySubscribeBinding.run{
            subscribeMaterialToolbar.run{
            }

            subscribeUserRecyclerView.run{
                adapter = SubscribeUserListAdapter()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

            subscribeUserReviewRecyclerView.run{
                adapter = UserReviewAdapter()
                layoutManager = LinearLayoutManager(this@SubscribeActivity)
            }
            subscribeViewModel.getSubscribeAll(userIdx)
        }

    }

    inner class SubscribeUserListAdapter: RecyclerView.Adapter<SubscribeUserListAdapter.SubscribeUserListViewHolder>(){
        inner class SubscribeUserListViewHolder(subscribeUserListItemBinding: SubscribeUserListItemBinding): ViewHolder(subscribeUserListItemBinding.root){
            val subscribeUserProfileImageView = subscribeUserListItemBinding.subscrobeUserProfileImageView
            val subscribeUserProfileNickNameTextView = subscribeUserListItemBinding.subscrobeUserProfileNickNametextView
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SubscribeUserListViewHolder {
            val subscribeUserListItemBinding = SubscribeUserListItemBinding.inflate(layoutInflater)
            val subscribeUserListViewHolder = SubscribeUserListViewHolder(subscribeUserListItemBinding)

            subscribeUserListItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return subscribeUserListViewHolder
        }

        override fun getItemCount(): Int {
            return rowSubscribeList.size
        }

        override fun onBindViewHolder(holder: SubscribeUserListViewHolder, position: Int) {
            if(rowSubscribeList[position].profile == null){
                holder.subscribeUserProfileImageView.setImageResource(R.drawable.sample_img)
            }else {
                holder.subscribeUserProfileImageView.setImageBitmap(rowSubscribeList[position].profile)
            }
            holder.subscribeUserProfileNickNameTextView.text = "${rowSubscribeList[position].nickname}"
        }

    }

    inner class UserReviewAdapter: RecyclerView.Adapter<UserReviewAdapter.UserReviewViewHolder>(){
        inner class UserReviewViewHolder(subscribeUserReviewItemBinding: SubscribeUserReviewItemBinding): ViewHolder(subscribeUserReviewItemBinding.root){
            var subscribeItemImageView = subscribeUserReviewItemBinding.subscribeItemImageView
            var subscribeItemNametextView = subscribeUserReviewItemBinding.subscribeItemNametextView
            var subscribeItemPriceTextView = subscribeUserReviewItemBinding.subscribeItemPriceTextView
            var subscribeUserProfileImageView = subscribeUserReviewItemBinding.subscribeUserProfileImageView
            val subscribeUserProfileNickNametextView = subscribeUserReviewItemBinding.subscribeUserProfileNickNametextView
            var subscribeUserProfileReviewContentsTextView = subscribeUserReviewItemBinding.subscribeUserProfileReviewContentsTextView
            var subscribeUserProfileReviewLikeCountTextView = subscribeUserReviewItemBinding.subscribeUserProfileReviewLikeCountTextView

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserReviewViewHolder {
            val subscribeUserReviewItemBinding = SubscribeUserReviewItemBinding.inflate(layoutInflater)
            val userReviewViewHolder = UserReviewViewHolder(subscribeUserReviewItemBinding)

            subscribeUserReviewItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return userReviewViewHolder
        }

        override fun getItemCount(): Int {
            return reviewBySubscribeViewModel.reviewList.value?.size!!
        }

        override fun onBindViewHolder(holder: UserReviewViewHolder, position: Int) {
            holder.subscribeItemImageView.setImageBitmap(reviewBySubscribeViewModel.reviewList.value?.get(position)?.productImg)
            holder.subscribeItemNametextView.text = reviewBySubscribeViewModel.reviewList.value?.get(position)?.productTitle
            holder.subscribeItemPriceTextView.text = reviewBySubscribeViewModel.reviewList.value?.get(position)?.productPrice
            if(reviewBySubscribeViewModel.reviewList.value?.get(position)?.profileImg == null){
                holder.subscribeUserProfileImageView.setImageResource(R.drawable.sample_img)
            }else {
                holder.subscribeUserProfileImageView.setImageBitmap(
                    reviewBySubscribeViewModel.reviewList.value?.get(
                        position
                    )?.profileImg
                )
            }
            holder.subscribeUserProfileReviewContentsTextView.text = reviewBySubscribeViewModel.reviewList.value?.get(position)?.reviewContext
            holder.subscribeUserProfileReviewLikeCountTextView.text = reviewBySubscribeViewModel.reviewList.value?.get(position)?.likeCnt
            holder.subscribeUserProfileNickNametextView.text = reviewBySubscribeViewModel.reviewList.value?.get(position)?.nickname
        }
    }

}

data class SubscribeClass(val followerIdx:String, val nickname:String, val profile:Bitmap?)
data class ReviewBySubscribeClass(
    var writerIdx:String, val productTitle:String, val productImg: Bitmap?, val productPrice:String,
    val profileImg: Bitmap?, val nickname:String, var reviewContext:String, var likeCnt:String)