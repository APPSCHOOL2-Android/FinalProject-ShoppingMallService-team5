package com.hifi.hifi_shopping.parcel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentReviewWriteBinding
import com.hifi.hifi_shopping.databinding.ReviewRycItemBinding
import com.hifi.hifi_shopping.parcel.vm.ReviewProductViewModel
import com.hifi.hifi_shopping.parcel.vm.ReviewSubscribeViewModel
import java.text.NumberFormat
import java.util.Locale

class ReviewWriteFragment : Fragment() {
    lateinit var fragmentReviewWriteBinding: FragmentReviewWriteBinding
    lateinit var reviewProductViewModel:ReviewProductViewModel
    lateinit var reviewSubscribeViewModel: ReviewSubscribeViewModel
    lateinit var parcelActivity: ParcelActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentReviewWriteBinding = FragmentReviewWriteBinding.inflate(inflater)
        parcelActivity = activity as ParcelActivity
        reviewProductViewModel = ViewModelProvider(this)[ReviewProductViewModel::class.java]
        reviewSubscribeViewModel = ViewModelProvider(this)[ReviewSubscribeViewModel::class.java]
        reviewProductViewModel.run{
            productName.observe(parcelActivity){
                fragmentReviewWriteBinding.reviewWriteItemTitleTextView.text = it
            }
            productPrice.observe(parcelActivity){
                fragmentReviewWriteBinding.reviewWriteItemPricetextView.text = formatPrice(it)
            }
            productImg.observe(parcelActivity){
                fragmentReviewWriteBinding.reviewWriteItemImageView.setImageBitmap(it)
            }
        }

        reviewSubscribeViewModel.run{
            subscribeList.observe(viewLifecycleOwner){
                fragmentReviewWriteBinding.reviewWriteItemRecommendHumanRecyclerView.adapter?.notifyDataSetChanged()
            }
        }

        fragmentReviewWriteBinding.run{
            // todo : 해당 상품 idx 입력 연결
            reviewProductViewModel.getProductByIdx("1")
            reviewSubscribeViewModel.getSubscribeListByUserIdx("0")
            reviewWriteToolbar.run{
                setNavigationOnClickListener {
                    parcelActivity.removeFragment(ParcelActivity.REVIEW_WRITE_FRAGMENT)
                }
            }

            reviewWriteItemRecommendHumanRecyclerView.run{
                adapter = RecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
        return fragmentReviewWriteBinding.root
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