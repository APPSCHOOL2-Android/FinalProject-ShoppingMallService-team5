package com.hifi.hifi_shopping.wish

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.databinding.FragmentWishBinding
import com.hifi.hifi_shopping.databinding.RowWishBinding
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.user.UserActivity
import com.hifi.hifi_shopping.user.repository.ProductImgRepository
import com.hifi.hifi_shopping.user.vm.WishViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class WishFragment : Fragment() {
    lateinit var fragmentWishBinding : FragmentWishBinding
    lateinit var categoryActivity: CategoryActivity

    var fromMyPage = false

    lateinit var wishViewModel: WishViewModel
    var isCheckedAll = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentWishBinding = FragmentWishBinding.inflate(inflater, container, false)
        categoryActivity = activity as CategoryActivity

        fromMyPage = arguments?.getBoolean("fromMyPage") ?: false

        if (fromMyPage) {
            categoryActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        categoryActivity.navController.popBackStack()
                        categoryActivity.categoryViewModel.setNavControllerDestination(categoryActivity.navController.currentDestination?.id ?: R.id.categoryMainFragment)
                    }

                    if (fromMyPage) {
                        val intent = Intent(categoryActivity, UserActivity::class.java)
                        intent.putExtra("userEmail", categoryActivity.userDataClass.email)
                        intent.putExtra("userIdx", categoryActivity.userDataClass.idx)
                        intent.putExtra("userNickname", categoryActivity.userDataClass.nickname)
                        intent.putExtra("userPw", categoryActivity.userDataClass.pw)
                        intent.putExtra("userVerify", categoryActivity.userDataClass.verify)
                        intent.putExtra("userPhoneNum", categoryActivity.userDataClass.phoneNum)
                        intent.putExtra("userProfileImg", categoryActivity.userDataClass.profileImg)
                        intent.putExtra("whereFrom", "category")
                        startActivity(intent)
                    }
                }
            })
        }            

        val userTemp = categoryActivity.userDataClass


        wishViewModel = ViewModelProvider(categoryActivity)[WishViewModel::class.java]
        wishViewModel.run {
            getWishListByUser(categoryActivity.userDataClass.idx)
            productDataList.observe(categoryActivity){
                if(productDataList.value?.size!! > 0){
                    fragmentWishBinding.run {
                        wishTextViewEmpty.visibility = View.GONE
                        wishScrollView.visibility = View.VISIBLE

                    }
                }
                fragmentWishBinding.run {
                    wishListItemCount.text = productDataList.value!!.size.toString()
                    wishListRecyclerView.run {
                        adapter = WishRecyclerViewAdapter()
                        layoutManager = GridLayoutManager(categoryActivity, 3)
                    }
                }
            }
        }



        fragmentWishBinding.run {
            wishToolbar.run {
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_item_search -> {
                            val intent = Intent(categoryActivity, SearchActivity::class.java)
                            startActivity(intent)
                        }
                        R.id.menu_item_cart -> {
                            val intent = Intent(categoryActivity, UserActivity::class.java)
                            intent.putExtra("whereFrom","wish")
                            intent.putExtra("userFragmentType","cart")
                            intent.putExtra("navigateTo",R.id.bottomMenuItemRankMain)
                            intent.putExtra("userEmail", categoryActivity.userDataClass.email)
                            intent.putExtra("userIdx", categoryActivity.userDataClass.idx)
                            intent.putExtra("userNickname", categoryActivity.userDataClass.nickname)
                            intent.putExtra("userPw", categoryActivity.userDataClass.pw)
                            intent.putExtra("userVerify", categoryActivity.userDataClass.verify)
                            intent.putExtra("userPhoneNum", categoryActivity.userDataClass.phoneNum)
                            intent.putExtra("userProfileImg", categoryActivity.userDataClass.profileImg)
                            startActivity(intent)
                        }
                    }
                    true
                }
            }
        }

        return fragmentWishBinding.root
    }




    inner class WishRecyclerViewAdapter() : RecyclerView.Adapter<WishRecyclerViewAdapter.WishRecyclerViewHolder>(){

        inner class WishRecyclerViewHolder(rowWishBinding: RowWishBinding) : RecyclerView.ViewHolder(rowWishBinding.root){

            val rowWishListItemName: TextView
            val rowWishListItemPrice: TextView
            val rowWishListImg: ImageView
            val rowWishListCheckbox: CheckBox


            init{
                rowWishListItemName= rowWishBinding.rowWishListItemName
                rowWishListItemPrice= rowWishBinding.rowWishListItemPrice
                rowWishListImg = rowWishBinding.rowWishListImg
                rowWishListCheckbox= rowWishBinding.rowWishListCheckbox
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishRecyclerViewHolder {
            val rowWishBinding = RowWishBinding.inflate(layoutInflater)
            val allViewHolder = WishRecyclerViewHolder(rowWishBinding)

            rowWishBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return allViewHolder
        }

        override fun getItemCount(): Int {
            return wishViewModel.productDataList.value?.size!!
        }

        override fun onBindViewHolder(holder: WishRecyclerViewHolder, position: Int) {
            val productidx = wishViewModel.productDataList.value?.get(position)?.idx!!
            holder.rowWishListItemName.text = wishViewModel.productDataList.value?.get(position)?.name
            holder.rowWishListItemPrice.text = wishViewModel.productDataList.value?.get(position)?.price+"원"
            holder.rowWishListImg.run {
                setOnClickListener {
                    categoryActivity.sendIntentProcuctUserInfo(productidx)
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
                            categoryActivity.runOnUiThread {
                                imgView.setImageBitmap(bitmap)
                            }
                        }
                    }
                }
            }
        }
    }




}