package com.hifi.hifi_shopping.buy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hifi.hifi_shopping.R

class BuyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val receivedIntent = intent
        if (receivedIntent != null && receivedIntent.hasExtra("cartProducts")) {
            // 장바구니에서 선택된 아이템들만 가져오기
            val cartProductsList = receivedIntent.getStringArrayListExtra("cartProducts")
            // 클릭한 아이템의 인덱스

            Log.d("구매장바구니",cartProductsList.toString())

        }

        if (receivedIntent != null && receivedIntent.hasExtra("productIdx")) {
            val productIdx = receivedIntent.getStringExtra("productIdx")
            Log.d("구매장바구니2",productIdx.toString())

        }

        setContentView(R.layout.activity_buy)
    }
}