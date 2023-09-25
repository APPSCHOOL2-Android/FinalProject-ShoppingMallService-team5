package com.hifi.hifi_shopping.user

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.databinding.FragmentPurchaseBinding
import com.hifi.hifi_shopping.databinding.RowPurchaseBinding
import com.hifi.hifi_shopping.databinding.RowUserPageItemBinding
import com.hifi.hifi_shopping.review.ReviewActivity
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.user.model.ReviewDataClass
import com.hifi.hifi_shopping.user.repository.ProductImgRepository
import com.hifi.hifi_shopping.user.repository.ReviewRepository
import com.hifi.hifi_shopping.user.vm.OrderViewModel
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class PurchaseFragment : Fragment() {

    lateinit var fragmentPurchaseBinding: FragmentPurchaseBinding
    lateinit var userActivity: UserActivity
    lateinit var orderViewModel: OrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPurchaseBinding = FragmentPurchaseBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity
        orderViewModel = ViewModelProvider(userActivity)[OrderViewModel::class.java]

        val userLogin = userActivity.userTemp
        val userPagesUser= userActivity.userTemp
        //        val userPagesUser = UserDataClass("2", "alohalo98@naver.com", "1222", "테스트계정", "false", "010-2222-2222", "")

        orderViewModel.run {
            getOrderListByUser(userLogin.idx)
            productDataList.observe(userActivity){
                fragmentPurchaseBinding.run {
                    purchaseRecyclerView.run {
                        adapter = PurchaseRecyclerViewAdapter()
                        layoutManager = LinearLayoutManager(userActivity, LinearLayoutManager.HORIZONTAL, false)
                    }
                }
            }
        }

        fragmentPurchaseBinding.run {
            purchaseToolbar.run {
                setNavigationOnClickListener{
                    userActivity.removeFragment(UserActivity.PURCHASE_FRAGMENT)
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

        }
        return fragmentPurchaseBinding.root
    }

    inner class PurchaseRecyclerViewAdapter : RecyclerView.Adapter<PurchaseRecyclerViewAdapter.PurchaseRecyclerViewHolder>(){

        inner class PurchaseRecyclerViewHolder(rowPurchaseBinding: RowPurchaseBinding) : RecyclerView.ViewHolder(rowPurchaseBinding.root){

            val rowPurchaseName: TextView
            val rowPurchasePoint: TextView
            val rowPurchaseReview: TextView
            val rowPurchaseDate: TextView
            val rowPurchaseRatingBar: RatingBar
            val rowPurchaseImg: ImageView
            val rowPurchaseReviewGroup: CardView
            val rowPurchaseBtnReview: Button
            val rowPurchaseBtnOrder: Button


            init{
                rowPurchaseName = rowPurchaseBinding.rowPurchaseName
                rowPurchasePoint = rowPurchaseBinding.rowPurchasePoint
                rowPurchaseReview = rowPurchaseBinding.rowPurchaseReview
                rowPurchaseRatingBar = rowPurchaseBinding.rowPurchaseRatingBar
                rowPurchaseImg = rowPurchaseBinding.rowPurchaseImg
                rowPurchaseReviewGroup = rowPurchaseBinding.rowPurchaseReviewGroup
                rowPurchaseDate = rowPurchaseBinding.rowPurchaseDate
                rowPurchaseBtnReview = rowPurchaseBinding.rowPurchaseBtnReview
                rowPurchaseBtnOrder = rowPurchaseBinding.rowPurchaseBtnOrder

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseRecyclerViewHolder {
            val rowPurchaseBinding = RowPurchaseBinding.inflate(layoutInflater)
            val allViewHolder = PurchaseRecyclerViewHolder(rowPurchaseBinding)

            rowPurchaseBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return allViewHolder
        }

        override fun getItemCount(): Int {
            return orderViewModel.productDataList.value?.size!!
        }

        override fun onBindViewHolder(holder: PurchaseRecyclerViewHolder, position: Int) {
            val productidx = orderViewModel.productDataList.value?.get(position)?.idx!!
            holder.rowPurchaseName.text = orderViewModel.productDataList.value?.get(position)?.name
            holder.rowPurchasePoint.text = orderViewModel.productDataList.value?.get(position)?.pointAmount
            holder.rowPurchaseDate.text = orderViewModel.orderDataList.value?.get(position)?.date
            holder.rowPurchaseBtnReview.setOnClickListener {
                val intent = Intent(userActivity, ReviewActivity::class.java)
                intent.putExtra("productIdx", productidx)
                startActivity(intent)
            }

            holder.rowPurchaseBtnOrder.setOnClickListener {
                val buyProduct = arrayListOf(productidx)
                userActivity.clickProductImg(buyProduct,userActivity.userTemp)
            }

            val userTemp = userActivity.userTemp
            ReviewRepository.getReviewAllByUserIdx(userTemp.idx){
                for (c1 in it.result.children) {
                    if(c1.child("productIdx").value == productidx){
                        holder.rowPurchaseReview.text = c1.child("context").value as String
                        val score = c1.child("score").value as String
                        holder.rowPurchaseRatingBar.rating = score.toFloat()
                    } else{
                        holder.rowPurchaseReviewGroup.visibility = View.GONE
                    }
                }
            }


            holder.rowPurchaseImg.run {
                setOnClickListener {
                    val buyProduct = arrayListOf(productidx)
                    userActivity.clickProductImg(buyProduct,userActivity.userTemp)
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