package com.hifi.hifi_shopping.notice

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColor
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivityNoticeBinding

class NoticeActivity : AppCompatActivity() {

    lateinit var activityNoticeBinding: ActivityNoticeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uiSetting()
    }
    fun uiSetting(){

        activityNoticeBinding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(activityNoticeBinding.root)

        activityNoticeBinding.run{
            noticeMaterialToolbar.run{
                title = "알림"
                setNavigationIcon(R.drawable.chevron_left_24px)
                inflateMenu(R.menu.toolbar_menu_basic)
                isTitleCentered = true
            }

            noticeAllButton.run{
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

            noticeOrderPareceButton.run{
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

            noticeSubscribeButton.run{
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

            noticeWishListButton.run{
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
}