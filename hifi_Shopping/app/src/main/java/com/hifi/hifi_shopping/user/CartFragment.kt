package com.hifi.hifi_shopping.user

import CartViewModel
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.databinding.FragmentCartBinding
import com.hifi.hifi_shopping.databinding.RowCartItemBinding
import com.hifi.hifi_shopping.user.repository.ProductImgRepository
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class CartFragment : Fragment() {
   lateinit var fragmentCartBinding: FragmentCartBinding
   lateinit var userActivity: UserActivity
   lateinit var cartViewModel : CartViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentCartBinding = FragmentCartBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity
        val userTemp = userActivity.userTemp
        cartViewModel = ViewModelProvider(userActivity)[CartViewModel::class.java]


        cartViewModel.run{
            getCartProductList(userTemp.idx)
            cartProductList.observe(viewLifecycleOwner, Observer { newDataList ->
                fragmentCartBinding.run {
                    cartItemLayout.removeAllViews()
                    cartItemsAllCount.text = "${cartProductList.value?.size}개"
                    if (cartProductList.value?.isNotEmpty() == true){
                        cartTextViewEmpty.visibility = View.GONE
                    }
                    cartProductList.value?.forEach {
                        val rowCartItemBinding = RowCartItemBinding.inflate(LayoutInflater.from(userActivity))

                        // 데이터 설정
                        rowCartItemBinding.run {
                            rowCartItemName.text = it.name
                            rowCartItemPrice.text = it.price + "원"

                            ProductImgRepository.getProductImgInfoByProductIdx(it.idx) {
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
                                                    rowCartItemImg.setImageBitmap(bitmap)
                                                }
                                            }
                                        }
                                    }
                                }
                            }


                        }

                        cartItemLayout.addView(rowCartItemBinding.root)
                    }

                }
            })
        }


        return fragmentCartBinding.root
    }

}