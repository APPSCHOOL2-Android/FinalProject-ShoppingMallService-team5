package com.hifi.hifi_shopping.review


import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract.Colors
import androidx.core.graphics.toColor
import androidx.core.view.forEach
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivityReviewBinding


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
                menu.forEach {

                }
                isTitleCentered = true

            }
        }
    }

}
