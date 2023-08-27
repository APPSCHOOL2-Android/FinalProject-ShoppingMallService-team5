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
            val cartProductsList = receivedIntent.getStringArrayListExtra("cartProducts")
            // 장바구니에서 선택된 아이템들만 가져오기
            Log.d("장바구니 테스트",cartProductsList.toString())

        } else {
            // 데이터를 전달받지 못한 경우 또는 키가 일치하지 않는 경우의 처리
        }

        setContentView(R.layout.activity_buy)
    }
}