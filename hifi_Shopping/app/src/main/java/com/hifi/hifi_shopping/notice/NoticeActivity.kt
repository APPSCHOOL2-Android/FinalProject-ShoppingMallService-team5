package com.hifi.hifi_shopping.notice

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivityNoticeBinding
import com.hifi.hifi_shopping.databinding.NoticeRecycItemBinding

class NoticeActivity : AppCompatActivity() {

    lateinit var activityNoticeBinding: ActivityNoticeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uiSetting()
    }
    private fun uiSetting(){
        activityNoticeBinding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(activityNoticeBinding.root)

        toolbarSetting()
        buttonSetting()
        recyclerViewSetting()

    }

    private fun recyclerViewSetting(){
        activityNoticeBinding.run{
            noticeRecyclerView.run{
                adapter = NoticeAdapter()
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL))
            }
        }
    }

    private fun toolbarSetting(){
        activityNoticeBinding.run{
            noticeMaterialToolbar.run{
                title = "알림"
                setNavigationIcon(R.drawable.chevron_left_24px)
                inflateMenu(R.menu.toolbar_menu_basic)
                isTitleCentered = true
            }
        }
    }

    private fun buttonSetting(){
        activityNoticeBinding.run {
            noticeAllButton.run {
                setOnClickListener {
                    noticeAllButton.setBackgroundResource(R.drawable.custom_select_button)
                    noticeAllButton.setTextColor(Color.WHITE)
                    noticeOrderPareceButton.setBackgroundResource(R.drawable.custom_normal_button)
                    noticeOrderPareceButton.setTextColor(getColor(R.color.lstButtonTextGrayColor))
                    noticeSubscribeButton.setBackgroundResource(R.drawable.custom_normal_button)
                    noticeSubscribeButton.setTextColor(getColor(R.color.lstButtonTextGrayColor))
                    noticeWishListButton.setBackgroundResource(R.drawable.custom_normal_button)
                    noticeWishListButton.setTextColor(getColor(R.color.lstButtonTextGrayColor))
                }
            }

            noticeOrderPareceButton.run {
                setOnClickListener {
                    noticeAllButton.setBackgroundResource(R.drawable.custom_normal_button)
                    noticeAllButton.setTextColor(getColor(R.color.lstButtonTextGrayColor))
                    noticeOrderPareceButton.setBackgroundResource(R.drawable.custom_select_button)
                    noticeOrderPareceButton.setTextColor(Color.WHITE)
                    noticeSubscribeButton.setBackgroundResource(R.drawable.custom_normal_button)
                    noticeSubscribeButton.setTextColor(getColor(R.color.lstButtonTextGrayColor))
                    noticeWishListButton.setBackgroundResource(R.drawable.custom_normal_button)
                    noticeWishListButton.setTextColor(getColor(R.color.lstButtonTextGrayColor))
                }
            }

            noticeSubscribeButton.run {
                setOnClickListener {
                    noticeAllButton.setBackgroundResource(R.drawable.custom_normal_button)
                    noticeAllButton.setTextColor(getColor(R.color.lstButtonTextGrayColor))
                    noticeOrderPareceButton.setBackgroundResource(R.drawable.custom_normal_button)
                    noticeOrderPareceButton.setTextColor(getColor(R.color.lstButtonTextGrayColor))
                    noticeSubscribeButton.setBackgroundResource(R.drawable.custom_select_button)
                    noticeSubscribeButton.setTextColor(Color.WHITE)
                    noticeWishListButton.setBackgroundResource(R.drawable.custom_normal_button)
                    noticeWishListButton.setTextColor(getColor(R.color.lstButtonTextGrayColor))
                }
            }

            noticeWishListButton.run {
                setOnClickListener {
                    noticeAllButton.setBackgroundResource(R.drawable.custom_normal_button)
                    noticeAllButton.setTextColor(getColor(R.color.lstButtonTextGrayColor))
                    noticeOrderPareceButton.setBackgroundResource(R.drawable.custom_normal_button)
                    noticeOrderPareceButton.setTextColor(getColor(R.color.lstButtonTextGrayColor))
                    noticeSubscribeButton.setBackgroundResource(R.drawable.custom_normal_button)
                    noticeSubscribeButton.setTextColor(getColor(R.color.lstButtonTextGrayColor))
                    noticeWishListButton.setBackgroundResource(R.drawable.custom_select_button)
                    noticeWishListButton.setTextColor(Color.WHITE)
                }
            }
        }
    }

    inner class NoticeAdapter: RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {
        inner class NoticeViewHolder(noticeRecycItemBinding: NoticeRecycItemBinding) : ViewHolder(noticeRecycItemBinding.root){
            var noticeTitleImageView = noticeRecycItemBinding.noticeTitleImageView
            var noticeCheckImageView = noticeRecycItemBinding.noticeCheckImageView
            var noticeTitleTextView = noticeRecycItemBinding.noticeTitleTextView
            var noticeContentTextView = noticeRecycItemBinding.noticeContentTextView
            var noticeDetailedImageView = noticeRecycItemBinding.noticeDetailedImageView
            var noticeCategoryButton = noticeRecycItemBinding.noticeCategoryButton
            var noticeDateTextView = noticeRecycItemBinding.noticeDateTextView

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
            val noticeRecycItemBinding = NoticeRecycItemBinding.inflate(layoutInflater)
            val noticeViewHolder = NoticeViewHolder(noticeRecycItemBinding)

            noticeRecycItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return noticeViewHolder
        }

        override fun getItemCount(): Int {
            return 5
        }

        override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
            holder.noticeTitleImageView.setImageResource(R.drawable.hobby2)
            holder.noticeCheckImageView.setBackgroundResource(R.drawable.item_notice_uncheck)
            holder.noticeTitleTextView.text = "알림 제목"
            holder.noticeContentTextView.text = "알림 내용 알림 내용 알림 내용 알림 내용 알림 내용 알림 내용"
            holder.noticeDetailedImageView.setImageResource(R.drawable.chevron_right_24px)
            holder.noticeCategoryButton.text = "알림 카테고리"
            holder.noticeDateTextView.text = "알림 발생 날짜"
        }
    }

}