package com.hifi.hifi_shopping.user

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.databinding.FragmentUserPageItemBinding
import com.hifi.hifi_shopping.databinding.RowUserPageItemBinding
import com.hifi.hifi_shopping.user.repository.ProductImgRepository
import com.hifi.hifi_shopping.user.vm.OrderViewModel
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class UserPageItemFragment : Fragment() {
    lateinit var userActivity:UserActivity
    lateinit var fragmentUserPageItemBinding: FragmentUserPageItemBinding
    lateinit var orderViewModel : OrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUserPageItemBinding = FragmentUserPageItemBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity
        orderViewModel = ViewModelProvider(userActivity)[OrderViewModel::class.java]

        val userLogin = userActivity.userTemp
        val userPagesUser= userActivity.userTemp
        //        val userPagesUser = UserDataClass("2", "alohalo98@naver.com", "1222", "테스트계정", "false", "010-2222-2222", "")

        orderViewModel.run {
            getOrderListByUser(userLogin.idx)
            productDataList.observe(userActivity){
                fragmentUserPageItemBinding.run {
                    userPageItemRecyclerView.run {
                        adapter = UserPageItemRecyclerViewAdapter()
                        layoutManager = GridLayoutManager(userActivity, 3)
                    }
                }
            }
        }
        return fragmentUserPageItemBinding.root
    }

    inner class UserPageItemRecyclerViewAdapter() : RecyclerView.Adapter<UserPageItemRecyclerViewAdapter.UserPageItemRecyclerViewHolder>(){

        inner class UserPageItemRecyclerViewHolder(rowUserPageItemBinding: RowUserPageItemBinding) : RecyclerView.ViewHolder(rowUserPageItemBinding.root){

            val rowUserPageItemName: TextView
            val rowUserPageItemImg: ImageView


            init{
                rowUserPageItemName = rowUserPageItemBinding.rowUserPageItemName
                rowUserPageItemImg = rowUserPageItemBinding.rowUserPageItemImg
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPageItemRecyclerViewHolder {
            val rowUserPageItemBinding = RowUserPageItemBinding.inflate(layoutInflater)
            val allViewHolder = UserPageItemRecyclerViewHolder(rowUserPageItemBinding)

            rowUserPageItemBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return allViewHolder
        }

        override fun getItemCount(): Int {
            return orderViewModel.productDataList.value?.size!!
        }

        override fun onBindViewHolder(holder: UserPageItemRecyclerViewHolder, position: Int) {
            val productidx = orderViewModel.productDataList.value?.get(position)?.idx!!
            holder.rowUserPageItemName.text = orderViewModel.productDataList.value?.get(position)?.name
            holder.rowUserPageItemImg.run {
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