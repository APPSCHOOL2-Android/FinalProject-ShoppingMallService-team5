package com.hifi.hifi_shopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hifi.hifi_shopping.databinding.ActivityMainBinding
import com.hifi.hifi_shopping.pacel.PacelActivity
import com.hifi.hifi_shopping.review.ReviewActivity
import com.hifi.hifi_shopping.subscribe.SubscribeActivity

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val intent = Intent(this@MainActivity, SubscribeActivity::class.java)
        startActivity(intent)

    }
}