package com.hifi.hifi_shopping.review


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.hifi.hifi_shopping.databinding.ActivityReviewBinding



class ReviewActivity : AppCompatActivity() {

    lateinit var activityReviewBinding: ActivityReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityReviewBinding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(activityReviewBinding.root)

    }

}
