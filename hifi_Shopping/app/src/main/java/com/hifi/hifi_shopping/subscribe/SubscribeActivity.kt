package com.hifi.hifi_shopping.subscribe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivitySubscribeBinding
import com.hifi.hifi_shopping.databinding.SubscribeUserListItemBinding
import com.hifi.hifi_shopping.databinding.SubscribeUserReviewItemBinding
import com.hifi.hifi_shopping.review.ReviewActivity

class SubscribeActivity : AppCompatActivity() {

    lateinit var activitySubscribeBinding: ActivitySubscribeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiSetting()
    }
    fun uiSetting(){
        activitySubscribeBinding = ActivitySubscribeBinding.inflate(layoutInflater)
        setContentView(activitySubscribeBinding.root)

        activitySubscribeBinding.run{
            subscribeMaterialToolbar.run{
                title = "구독 리스트"
                setNavigationIcon(R.drawable.chevron_left_24px)
                inflateMenu(R.menu.toolbar_menu_basic)
                isTitleCentered = true
            }

            subscribeUserRecyclerView.run{
                adapter = SubscribeUserListAdapter()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

            subscribeUserReviewRecyclerView.run{
                adapter = UserReviewAdapter()
                layoutManager = LinearLayoutManager(this@SubscribeActivity)
            }

        }

    }

    inner class SubscribeUserListAdapter: RecyclerView.Adapter<SubscribeUserListAdapter.SubscribeUserListViewHolder>(){
        inner class SubscribeUserListViewHolder(subscribeUserListItemBinding: SubscribeUserListItemBinding): ViewHolder(subscribeUserListItemBinding.root){
            val subscrobeUserProfileImageView = subscribeUserListItemBinding.subscrobeUserProfileImageView
            val subscrobeUserProfileNickNametextView = subscribeUserListItemBinding.subscrobeUserProfileNickNametextView
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
            return 10
        }

        override fun onBindViewHolder(holder: SubscribeUserListViewHolder, position: Int) {
            holder.subscrobeUserProfileImageView.setImageResource(R.drawable.prepare)
            holder.subscrobeUserProfileNickNametextView.text = "${position}번 인플러언서"
        }

    }

    inner class UserReviewAdapter: RecyclerView.Adapter<UserReviewAdapter.UserReviewViewHolder>(){
        inner class UserReviewViewHolder(subscribeUserReviewItemBinding: SubscribeUserReviewItemBinding): ViewHolder(subscribeUserReviewItemBinding.root){
            var subscrobeItemImageView = subscribeUserReviewItemBinding.subscrobeItemImageView
            var subscrobeItemNametextView = subscribeUserReviewItemBinding.subscrobeItemNametextView
            var subscrobeItemPriceTextView = subscribeUserReviewItemBinding.subscrobeItemPriceTextView
            var subscrobeUserProfileImageView = subscribeUserReviewItemBinding.subscrobeUserProfileImageView
            var subscrobeUserProfileReviewContentsTextView = subscribeUserReviewItemBinding.subscrobeUserProfileReviewContentsTextView
            var subscrobeUserProfileReviewLikeCountTextView = subscribeUserReviewItemBinding.subscrobeUserProfileReviewLikeCountTextView

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
            return 10
        }

        override fun onBindViewHolder(holder: UserReviewViewHolder, position: Int) {
            holder.subscrobeItemImageView.setImageResource(R.drawable.manage)
            holder.subscrobeItemNametextView.text = "TMA-2 Comfort Wireless"
            holder.subscrobeItemPriceTextView.text = "20,000 원"
            holder.subscrobeUserProfileImageView.setImageResource(R.drawable.hobby2)
            holder.subscrobeUserProfileReviewContentsTextView.text = """이 물건은 ~~~ 이물건!!!
                |이 물건은 ~~~ 이물건!!!
                |이 물건은 ~~~ 이물건!!!
                |이 물건은 ~~~ 이물건!!!
                |이 물건은 ~~~ 이물건!!!
            """.trimMargin()
            holder.subscrobeUserProfileReviewLikeCountTextView.text = "264"
        }
    }

}