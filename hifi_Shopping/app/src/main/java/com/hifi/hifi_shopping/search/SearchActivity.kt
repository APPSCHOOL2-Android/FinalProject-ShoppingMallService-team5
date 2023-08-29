package com.hifi.hifi_shopping.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {

        }
    }
}