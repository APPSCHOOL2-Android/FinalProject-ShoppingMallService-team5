package com.hifi.hifi_shopping.review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.ActivityReviewBinding
import com.hifi.hifi_shopping.databinding.ReviewRycItemBinding
import com.hifi.hifi_shopping.review.vm.ReviewProductViewModel
import com.hifi.hifi_shopping.review.vm.ReviewSubscribeViewModel
import java.text.NumberFormat
import java.util.Locale

class ReviewActivity : AppCompatActivity() {
    lateinit var activityReviewBinding: ActivityReviewBinding
    lateinit var reviewProductViewModel:ReviewProductViewModel
    lateinit var reviewSubscribeViewModel: ReviewSubscribeViewModel
    lateinit var productIdx: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityReviewBinding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(activityReviewBinding.root)

        val receivedIntent = intent
        if (receivedIntent != null && receivedIntent.hasExtra("productIdx")) {
            productIdx = receivedIntent.getStringExtra("productIdx")!!
            Log.d("리뷰 데이터",productIdx.toString())

        }

        reviewProductViewModel = ViewModelProvider(this)[ReviewProductViewModel::class.java]
        reviewSubscribeViewModel = ViewModelProvider(this)[ReviewSubscribeViewModel::class.java]
        reviewProductViewModel.run{
            productName.observe(this@ReviewActivity){
                activityReviewBinding.reviewWriteItemTitleTextView.text = it
            }
            productPrice.observe(this@ReviewActivity){
                activityReviewBinding.reviewWriteItemPricetextView.text = formatPrice(it)
            }
            productImg.observe(this@ReviewActivity){
                activityReviewBinding.reviewWriteItemImageView.setImageBitmap(it)
            }
        }

        reviewSubscribeViewModel.run{
            subscribeList.observe(this@ReviewActivity){
                activityReviewBinding.reviewWriteItemRecommendHumanRecyclerView.adapter?.notifyDataSetChanged()
            }
        }

        activityReviewBinding.run{
            // todo : 해당 상품 idx 입력 연결
            reviewProductViewModel.getProductByIdx(productIdx)
            reviewSubscribeViewModel.getSubscribeListByUserIdx("0")
            reviewWriteToolbar.run{
                setNavigationOnClickListener {

                }
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
            return reviewSubscribeViewModel.subscribeList.value?.size!!
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if(reviewSubscribeViewModel.subscribeList.value?.get(position)?.profile == null) {
                holder.profile.setImageResource(R.drawable.empty_photo)
            }else{
                holder.profile.setImageBitmap(reviewSubscribeViewModel.subscribeList.value?.get(position)?.profile)
            }
            holder.nickname.text = reviewSubscribeViewModel.subscribeList.value?.get(position)?.nickname
        }
    }

    fun formatPrice(priceStr: String): String {
        val price = priceStr.toIntOrNull() ?: return "Invalid Input"
        val formatter = NumberFormat.getNumberInstance(Locale("ko", "KR"))
        val formattedAmount = formatter.format(price)
        return "$formattedAmount 원"
    }
}