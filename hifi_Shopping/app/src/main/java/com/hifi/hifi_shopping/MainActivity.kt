package com.hifi.hifi_shopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hifi.hifi_shopping.auth.AuthActivity
import com.hifi.hifi_shopping.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val intent = Intent(baseContext, AuthActivity::class.java)
        startActivity(intent)

    }
}