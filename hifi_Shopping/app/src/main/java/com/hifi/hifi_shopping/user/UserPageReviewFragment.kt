package com.hifi.hifi_shopping.user

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.databinding.FragmentUserPageItemBinding
import com.hifi.hifi_shopping.databinding.FragmentUserPageReviewBinding
import com.hifi.hifi_shopping.databinding.RowUserPageItemBinding
import com.hifi.hifi_shopping.databinding.RowUserPageReviewBinding
import com.hifi.hifi_shopping.user.repository.ProductImgRepository
import com.hifi.hifi_shopping.user.repository.ProductRepository
import com.hifi.hifi_shopping.user.vm.OrderViewModel
import com.hifi.hifi_shopping.user.vm.ReviewViewModel
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class UserPageReviewFragment : Fragment() {
    lateinit var userActivity: UserActivity
    lateinit var fragmentUserPageReviewBinding : FragmentUserPageReviewBinding
    lateinit var reviewViewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUserPageReviewBinding = FragmentUserPageReviewBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity
        reviewViewModel = ViewModelProvider(userActivity)[ReviewViewModel::class.java]

        val userLogin = userActivity.userTemp
        val userPagesUser= userActivity.userTemp
        //        val userPagesUser = UserDataClass("2", "alohalo98@naver.com", "1222", "테스트계정", "false", "010-2222-2222", "")

        reviewViewModel.run {
            getReviewListByUser(userPagesUser.idx)
            reviewDataList.observe(userActivity){
                fragmentUserPageReviewBinding.run {
                    userPageReviewRecyclerView.run {
                        adapter = UserPageReviewRecyclerViewAdapter()
                        layoutManager = LinearLayoutManager(userActivity, LinearLayoutManager.HORIZONTAL, false)
                    }
                }
            }
        }
        return fragmentUserPageReviewBinding.root
    }

    inner class UserPageReviewRecyclerViewAdapter() : RecyclerView.Adapter<UserPageReviewRecyclerViewAdapter.UserPageReviewRecyclerViewHolder>(){

        inner class UserPageReviewRecyclerViewHolder(rowUserPageReviewBinding: RowUserPageReviewBinding) : RecyclerView.ViewHolder(rowUserPageReviewBinding.root){

            val rowUserPageReviewContext: TextView
            val rowUserPageReviewRecommendCount: TextView
            val rowPurchaseName: TextView
            val rowPurchasePrice: TextView
            val rowPurchaseImg: ImageView



            init{
                rowUserPageReviewContext = rowUserPageReviewBinding.rowPurchaseReview
                rowUserPageReviewRecommendCount = rowUserPageReviewBinding.rowPurchaseRecommendCount
                rowPurchaseName = rowUserPageReviewBinding.rowPurchaseName
                rowPurchasePrice = rowUserPageReviewBinding.rowPurchasePrice
                rowPurchaseImg = rowUserPageReviewBinding.rowPurchaseImg
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPageReviewRecyclerViewHolder {
            val rowUserPageReviewBinding = RowUserPageReviewBinding.inflate(layoutInflater)
            val allViewHolder = UserPageReviewRecyclerViewHolder(rowUserPageReviewBinding)

            rowUserPageReviewBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return allViewHolder
        }

        override fun getItemCount(): Int {
            return reviewViewModel.reviewDataList.value?.size!!
        }


        override fun onBindViewHolder(holder: UserPageReviewRecyclerViewHolder, position: Int) {
            val productidx = reviewViewModel.reviewDataList.value?.get(position)?.productIdx!!
            holder.rowUserPageReviewContext.text = reviewViewModel.reviewDataList.value?.get(position)?.context
            holder.rowUserPageReviewRecommendCount.text = reviewViewModel.reviewDataList.value?.get(position)?.likeCnt
            ProductRepository.getProductInfoByIdx(productidx){
                for(c1 in it.result.children){
                    holder.rowPurchaseName.text = c1.child("name").value as String
                    holder.rowPurchasePrice.text = c1.child("price").value as String + "원"
                }
            }
            holder.rowPurchaseImg.run {
                setOnClickListener {
                    val intent = Intent(userActivity, BuyActivity::class.java)
                    intent.putExtra("productIdx", productidx)
                    startActivity(intent)
                }
                getProductImg(productidx,this)
            }

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