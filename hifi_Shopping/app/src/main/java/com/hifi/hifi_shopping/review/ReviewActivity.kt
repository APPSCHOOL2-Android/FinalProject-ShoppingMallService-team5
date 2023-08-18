package com.hifi.hifi_shopping.review


import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.toColor
import androidx.core.view.forEach
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivityReviewBinding
import com.hifi.hifi_shopping.databinding.ReviewRycItemBinding


class ReviewActivity : AppCompatActivity() {

    lateinit var activityReviewBinding: ActivityReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityReviewBinding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(activityReviewBinding.root)

        activityReviewBinding.run{
            reviewWriteActivityToolbar.run{
                title = "후기 작성"
                setNavigationIcon(R.drawable.chevron_left_24px)
                inflateMenu(R.menu.review_write_menu)
                isTitleCentered = true
            }

            reviewWriteItemImageView.run{
                clipToOutline = true
            }

            reviewWriteItemRecommendHumanRecyclerView.run{
                adapter = RecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    inner class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
        inner class ViewHolder(reviewRycItemBinding: ReviewRycItemBinding) : RecyclerView.ViewHolder(reviewRycItemBinding.root){
            var profile : ImageView
            var nickname : TextView
            init{
                profile = reviewRycItemBinding.editUserProfileImg
                nickname = reviewRycItemBinding.textView5
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val reviewRycItemBinding = ReviewRycItemBinding.inflate(layoutInflater)
            val viewHolder = ViewHolder(reviewRycItemBinding)

            reviewRycItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return viewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.profile.setImageResource(R.drawable.couple)
            holder.nickname.text = "${position}번"
        }
    }

}