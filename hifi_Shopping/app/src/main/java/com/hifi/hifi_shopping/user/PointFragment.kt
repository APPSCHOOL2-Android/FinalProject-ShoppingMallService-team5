package com.hifi.hifi_shopping.user

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.databinding.FragmentPointBinding
import com.hifi.hifi_shopping.databinding.RowPointBinding
import com.hifi.hifi_shopping.databinding.RowPointReviewBinding
import com.hifi.hifi_shopping.review.ReviewActivity
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.user.repository.ProductImgRepository
import com.hifi.hifi_shopping.user.repository.ProductRepository
import com.hifi.hifi_shopping.user.vm.OrderViewModel
import com.hifi.hifi_shopping.user.vm.PointViewModel
import com.hifi.hifi_shopping.user.vm.ReviewViewModel
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class PointFragment : Fragment() {
    lateinit var fragmentPointBinding: FragmentPointBinding
    lateinit var userActivity: UserActivity
    lateinit var pointViewModel: PointViewModel
    lateinit var orderViewModel: OrderViewModel
    lateinit var reviewViewModel: ReviewViewModel
    var isToggled = false

    private val rowPointReviewBindingList = mutableListOf<RowPointReviewBinding>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPointBinding = FragmentPointBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity
        pointViewModel = ViewModelProvider(userActivity)[PointViewModel::class.java]
        orderViewModel = ViewModelProvider(userActivity)[OrderViewModel::class.java]
        reviewViewModel = ViewModelProvider(userActivity)[ReviewViewModel::class.java]

        val userTemp = userActivity.userTemp

        reviewViewModel.run{
             getReviewListByUser(userTemp.idx)
        }
        pointViewModel.run {
            getPointListByUser(userTemp.idx)
            pointDataList.observe(userActivity) {
                fragmentPointBinding.run {
                    pointUserPoint.text =
                        pointDataList.value?.map { it.amount.toInt() }?.sum().toString() + "P"
                    pointDetailRecyclerView.run {
                        adapter = PointRecyclerViewAdapter()
                        layoutManager =
                            LinearLayoutManager(userActivity, LinearLayoutManager.VERTICAL, false)
                    }
                }
            }
        }
        
        orderViewModel.run { 
            getOrderListByUser(userTemp.idx)
            orderDataList.observe(userActivity){
                fragmentPointBinding.run { 
                    pointReviewLayout.removeAllViews()
                    orderDataList.value?.forEach {
                        val productidx = it.productIdx
                        val foundItem = reviewViewModel.reviewDataList.value?.firstOrNull { it.productIdx == productidx }
                        if(foundItem==null) {
                            val rowPointReviewBinding =
                                RowPointReviewBinding.inflate(LayoutInflater.from(userActivity))

                            val userData = userActivity.userTemp
                            // 데이터 설정
                            rowPointReviewBinding.run {
                                rowPointReviewDate.text = it.date + " 결제"
                                rowPointReviewIdx.text = it.productIdx
                                ProductRepository.getProductInfoByIdx(it.productIdx) {
                                    for (c1 in it.result.children) {
                                        rowPointReviewName.text = c1.child("name").value as String
                                    }
                                }

                                rowPointReviewPrice.text = it.price + "원"
                                // 제품 이미지
                                getProductImg(it.idx, rowPointReviewImg)
                                rowPointReviewImg.setOnClickListener {
                                    val intent = Intent(userActivity, BuyActivity::class.java)
                                    intent.putExtra("productIdx", rowPointReviewIdx.text.toString())
                                    startActivity(intent)
                                }
                                rowPointReviewBtnReview.setOnClickListener {
                                    val intent = Intent(userActivity, ReviewActivity::class.java)
                                    intent.putExtra("productIdx", rowPointReviewIdx.text.toString())
                                    intent.putExtra("userEmail", userData.email)
                                    intent.putExtra("userIdx", userData.idx)
                                    intent.putExtra("userNickname", userData.nickname)
                                    intent.putExtra("userPw", userData.pw)
                                    intent.putExtra("userVerify", userData.verify)
                                    intent.putExtra("userPhoneNum", userData.phoneNum)
                                    intent.putExtra("userProfileImg", userData.profileImg)
                                    startActivity(intent)
                                }
                            }
                            pointReviewLayout.addView(rowPointReviewBinding.root)
                            rowPointReviewBindingList.add(rowPointReviewBinding)
                        }
                    }
                }
            }
        }

        fragmentPointBinding.run {

            pointToolbar.run {
                setNavigationOnClickListener {
                    userActivity.removeFragment(UserActivity.CART_FRAGMENT)
                }
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_item_search -> {
                            val intent = Intent(userActivity, SearchActivity::class.java)
                            startActivity(intent)
                        }
                        R.id.menu_item_cart -> {
                            userActivity.replaceFragment(UserActivity.CART_FRAGMENT, true, null)
                        }
                    }
                    true
                }

            }

            pointGuideBtnToggle.setOnClickListener {
                toggleButtonClick(pointGuideBtnToggle,pointGuideTextView)
            }
            pointReviewBtnToggle.setOnClickListener {
                toggleButtonClick(pointReviewBtnToggle,pointReviewLayout)
            }
        }


        return fragmentPointBinding.root
    }

    inner class PointRecyclerViewAdapter:
        RecyclerView.Adapter<PointRecyclerViewAdapter.PointRecyclerViewHolder>() {

        inner class PointRecyclerViewHolder(rowPointBinding: RowPointBinding) :
            RecyclerView.ViewHolder(rowPointBinding.root) {

            val rowPointDate: TextView
            val rowPointContext: TextView
            val rowPointAmount: TextView


            init {
                rowPointDate = rowPointBinding.rowPointDate
                rowPointContext = rowPointBinding.rowPointContext
                rowPointAmount = rowPointBinding.rowPointAmount
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointRecyclerViewHolder {
            val rowPointBinding = RowPointBinding.inflate(layoutInflater)
            val allViewHolder = PointRecyclerViewHolder(rowPointBinding)

            rowPointBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return allViewHolder
        }

        override fun getItemCount(): Int {
            return pointViewModel.pointDataList.value?.size!!
        }

        override fun onBindViewHolder(holder: PointRecyclerViewHolder, position: Int) {
            holder.rowPointDate.text = pointViewModel.pointDataList.value?.get(position)?.date
            holder.rowPointContext.text = pointViewModel.pointDataList.value?.get(position)?.context
            holder.rowPointAmount.text =
                pointViewModel.pointDataList.value?.get(position)?.amount + "P"

        }
    }

    fun toggleButtonClick(btn: ImageButton, view: View) {
        isToggled = !isToggled
        if (isToggled) {
            // 토글이 활성화된 경우
            btn.setImageResource(R.drawable.expand_less_24px)
            view.visibility = View.VISIBLE
        } else {
            btn.setImageResource(R.drawable.expand_more_24px)
            view.visibility = View.GONE

        }
    }

    fun getProductImg(productidx: String, imgView: ImageView) {
        ProductImgRepository.getProductImgInfoByProductIdx(productidx) {
            for (c1 in it.result.children) {
                val imgSrc = c1.child("imgSrc").value as String
                val default = c1.child("default").value as String

                if (default == "true") {
                    val storage = FirebaseStorage.getInstance()
                    val fileRef = storage.reference.child("product/${imgSrc}")

                    // 데이터를 가져올 수 있는 경로를 가져온다.
                    fileRef.downloadUrl.addOnCompleteListener {
                        thread {
                            // 파일에 접근할 수 있는 경로를 이용해 URL 객체를 생성한다.
                            val url = URL(it.result.toString())
                            // 접속한다.
                            val httpURLConnection = url.openConnection() as HttpURLConnection
                            // 이미지 객체를 생성한다.
                            val bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)
                            userActivity.runOnUiThread {
                                imgView.setImageBitmap(bitmap)
                            }
                        }
                    }
                }
            }
        }
    }

}