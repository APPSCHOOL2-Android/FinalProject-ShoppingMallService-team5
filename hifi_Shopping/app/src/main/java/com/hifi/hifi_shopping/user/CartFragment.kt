package com.hifi.hifi_shopping.user

import CartViewModel
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.databinding.FragmentCartBinding
import com.hifi.hifi_shopping.databinding.RowCartItemBinding
import com.hifi.hifi_shopping.user.repository.ProductImgRepository
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class CartFragment : Fragment() {
    lateinit var fragmentCartBinding: FragmentCartBinding
    lateinit var userActivity: UserActivity
    lateinit var cartViewModel: CartViewModel

    // rowCartItemBinding 객체들을 저장하는 리스트
    private val rowCartItemBindingList = mutableListOf<RowCartItemBinding>()
    val cartRemoveItemList = mutableListOf<RowCartItemBinding>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentCartBinding = FragmentCartBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity
        val userTemp = userActivity.userTemp
        cartViewModel = ViewModelProvider(userActivity)[CartViewModel::class.java]

        cartViewModel.run {
            getCartProductList(userTemp.idx)
            cartProductList.observe(viewLifecycleOwner, Observer { newDataList ->
                fragmentCartBinding.run {
                    cartItemLayout.removeAllViews()
                    rowCartItemBindingList.clear()
                    if (cartProductList.value?.isNotEmpty() == true) {
                        cartTextViewEmpty.visibility = View.GONE
                    }

                    // 각 아이템의 RowCartItemBinding을 생성하고 리스트에 추가
                    cartProductList.value?.forEach {
                        val rowCartItemBinding =
                            RowCartItemBinding.inflate(LayoutInflater.from(userActivity))
                        // 데이터 설정
                        rowCartItemBinding.run {
                            rowCartItemIdx.text = it.idx
                            rowCartItemName.text = it.name
                            rowCartItemPrice.text = it.price
                            // 제품 이미지
                            getCartProductImg(it.idx, rowCartItemImg)
                        }
                        cartItemLayout.addView(rowCartItemBinding.root)
                        rowCartItemBindingList.add(rowCartItemBinding) // 리스트에 추가
                    }

                    Log.d("제품 추가3", rowCartItemBindingList.toString())

                    // 임시로 전체 체크박스 제거
//                    cartCheckBoxAll.visibility = View.GONE

                    // 전체 체크박스 상태 변경 시 장바구니 아이템 체크박스 상태 처리
                    cartCheckBoxAll.setOnCheckedChangeListener { buttonView, isChecked ->
                        rowCartItemBindingList.forEach { itemBinding ->
                            itemBinding.rowCartItemCheckBox.isChecked = isChecked
                        }
                    }

                    // 각 체크박스의 상태 관찰
                    var itemsPrice = 0
                    var itemsCount = 0
                    rowCartItemBindingList.forEach { itemBinding ->
                        itemBinding.rowCartItemCheckBox.setOnCheckedChangeListener { _, _ ->
                            updateFragmentCheckBoxState()
                            if (itemBinding.rowCartItemCheckBox.isChecked) {
                                val itemPrice = itemBinding.rowCartItemPrice.text.toString().toInt()
                                itemsCount += 1
                                itemsPrice += itemPrice
                                Log.d("제품 추가", itemPrice.toString())
                                Log.d("제품 추가1", itemsPrice.toString())
                            } else if (!itemBinding.rowCartItemCheckBox.isChecked) {
                                val itemPrice = itemBinding.rowCartItemPrice.text.toString().toInt()
                                itemsCount -= 1
                                itemsPrice -= itemPrice
                            }
                            cartPayBtn.run {
                                cartPayBtnCount.text = "Total ${itemsCount} Items"
                                cartPayBtnTotal.text = itemsPrice.toString() + "원"
                            }
                        }

                    }

                    cartBtnDelete.setOnClickListener {
                        Log.d("장바구니 테스트4",rowCartItemBindingList.toString())
                        rowCartItemBindingList.forEach {
                            if (it.rowCartItemCheckBox.isChecked) {
                                cartItemLayout.removeView(it.root)
                                cartRemoveItemList.add(it)
                            }
                        }
                        initCartData()
                        itemsCount = 0
                        itemsPrice = 0

                    }
                    Log.d("장바구니 테스트4",rowCartItemBindingList.toString())

                    cartItemsAllCount.run {
                        var itemsAllCount = rowCartItemBindingList.size
                        text = "전체 ${itemsAllCount}개"
                    }

                    cartPayBtn.setOnClickListener {
                        val cartProductIdxList =
                            rowCartItemBindingList.filter { it.rowCartItemCheckBox.isChecked }
                                .map { it.rowCartItemIdx.text.toString() } as ArrayList
                        val intent = Intent(userActivity, BuyActivity::class.java)
                        intent.putExtra("cartProducts", cartProductIdxList)
                        startActivity(intent)
                    }
                }
            })
        }

        fragmentCartBinding.run {
            cartToolbar.run {
                setNavigationOnClickListener {
                    userActivity.removeFragment(UserActivity.CART_FRAGMENT)
                }
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_item_home -> {
                            val intent = Intent(userActivity, CategoryActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    true
                }

            }
        }

        return fragmentCartBinding.root
    }

    fun getCartProductImg(productidx: String, imgView: ImageView) {
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


    private fun updateFragmentCheckBoxState() {
        // 모든 rowCartItemBinding의 체크박스 상태를 확인
        val allChecked = rowCartItemBindingList.all { itemBinding ->
            itemBinding.rowCartItemCheckBox.isChecked
        }
        fragmentCartBinding.cartCheckBoxAll.isChecked = allChecked

    }

    private fun initCartData(){
        rowCartItemBindingList.removeAll(cartRemoveItemList)
        fragmentCartBinding.run {
            cartItemsAllCount.text ="전체 ${rowCartItemBindingList.size}개"
            cartPayBtn.run {
                cartPayBtnCount.text = "Total 0 Items"
                cartPayBtnTotal.text="0원"
            }


        }
    }

}