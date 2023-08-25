package com.hifi.hifi_shopping.buy.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.auth.AuthActivity
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.buy.buy_repository.OrderUserRepository
import com.hifi.hifi_shopping.buy.buy_repository.OrderUserRepository.Companion.getOrderUserCoupon
import com.hifi.hifi_shopping.buy.buy_repository.OrderUserRepository.Companion.getOrderUserPossibleCoupon
import com.hifi.hifi_shopping.buy.buy_vm.OrderItemViewModel
import com.hifi.hifi_shopping.buy.buy_vm.OrderProduct
import com.hifi.hifi_shopping.buy.buy_vm.OrderUserCoupon
import com.hifi.hifi_shopping.buy.buy_vm.OrderUserViewModel
import com.hifi.hifi_shopping.buy.buy_vm.PossibleCoupon
import com.hifi.hifi_shopping.databinding.FragmentOrderBinding
import com.hifi.hifi_shopping.databinding.RowOrderItemListBinding
import kotlin.concurrent.thread


class OrderFragment : Fragment() {

    private lateinit var fragmentOrderBinding: FragmentOrderBinding
    private lateinit var buyActivity: BuyActivity

    private lateinit var orderUserViewModel: OrderUserViewModel
    private lateinit var orderItemViewModel: OrderItemViewModel
    private var orderUserIdx = ""
    private var selAddressIdx = 0
    private lateinit var orderItemList : ArrayList<String>
    private var orderProductList = mutableListOf<OrderProduct>()
    private lateinit var rowOrderItemListBinding: RowOrderItemListBinding

    private var totalOrderProductCount = 0
    private var totalOrderProductPrice = 0

    private var authCheck = true

    private val cardList = arrayOf(
        "은행 선택","신한", "농협", "국민", "우리", "카카오", "토스"
    )
    private val installmentMonth = arrayOf(
        "일시불", "2개월", "3개월", "4개월", "5개월", "6개월", "12개월", "24개월", "36개월"
    )

    private var userCouponList = mutableListOf<OrderUserCoupon>()
    private var possibleCouponList = mutableListOf<PossibleCoupon>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        dataSetting()
        viewModelSetting()
        viewSetting()
        clickEventSetting()

        return fragmentOrderBinding.root
    }


    override fun onResume() {
        super.onResume()
        orderProductList.clear()
    }

    private fun dataSetting(){
        fragmentOrderBinding = FragmentOrderBinding.inflate(layoutInflater)
        buyActivity = activity as BuyActivity
        orderUserIdx = arguments?.getString("userIdx")!!
        orderItemList = arguments?.getStringArrayList("selProduct")!!
        fragmentOrderBinding.orderPayBtnCount.text = "Total ${orderProductList.size} Items"
    }

    private fun clickEventSetting(){
        fragmentOrderBinding.run{

            orderUserBtnToggle.run{
                setOnClickListener {
                    orderUserLayoutAuthComplete.isVisible = !orderUserLayoutAuthComplete.isVisible
                    if (orderUserLayoutAuthComplete.isVisible) {
                        setImageResource(R.drawable.expand_less_24px)
                    } else {
                        setImageResource(R.drawable.expand_more_24px)
                    }
                }
            }

            fragmentOrderBinding.orderUserBtnAuth.setOnClickListener {
                fragmentOrderBinding.orderUserLayoutAuthComplete.visibility = View.VISIBLE
            }

            orderUserAuthCommit.setOnClickListener {
                if(orderUserViewModel.verify.value!!) return@setOnClickListener
                orderUserViewModel.setOrderUserAuth(orderUserIdx)
            }

            toolbarOrder.run{
                setOnMenuItemClickListener {// 임시
                    val intent = Intent(context, AuthActivity::class.java)
                    startActivity(intent)
                    true
                }
                setNavigationOnClickListener {
                    buyActivity.removeFragment(BuyActivity.ORDER_FRAGMENT)
                }
            }

            val selectFont = ResourcesCompat.getFont(buyActivity, R.font.notosanskr_bold)
            val nomalFont = ResourcesCompat.getFont(buyActivity, R.font.notosanskr_medium)
            orderDeliverBtnFirst.run{
                setOnClickListener {
                    setBackgroundResource(R.drawable.background_subscribe_button)
                    typeface = selectFont
                    setTextColor(Color.WHITE)
                    orderDeliverBtnSecond.setBackgroundResource(R.drawable.address_not_select_backround)
                    orderDeliverBtnSecond.setTextColor(buyActivity.getColor(R.color.lstButtonTextGrayColor))
                    orderDeliverBtnSecond.typeface = nomalFont
                    orderDeliverBtnThird.setBackgroundResource(R.drawable.address_not_select_backround)
                    orderDeliverBtnThird.setTextColor(buyActivity.getColor(R.color.lstButtonTextGrayColor))
                    orderDeliverBtnThird.typeface = nomalFont
                    selAddressIdx = 0
                    orderUserViewModel.lookOrderUserAddress(selAddressIdx)
                }
            }

            orderDeliverBtnSecond.run{
                setOnClickListener {
                    setBackgroundResource(R.drawable.background_subscribe_button)
                    typeface = selectFont
                    setTextColor(Color.WHITE)
                    orderDeliverBtnFirst.setBackgroundResource(R.drawable.address_not_select_backround)
                    orderDeliverBtnFirst.setTextColor(buyActivity.getColor(R.color.lstButtonTextGrayColor))
                    orderDeliverBtnFirst.typeface = nomalFont
                    orderDeliverBtnThird.setBackgroundResource(R.drawable.address_not_select_backround)
                    orderDeliverBtnThird.setTextColor(buyActivity.getColor(R.color.lstButtonTextGrayColor))
                    orderDeliverBtnThird.typeface = nomalFont
                    selAddressIdx = 1
                    orderUserViewModel.lookOrderUserAddress(selAddressIdx)
                }
            }

            orderDeliverBtnThird.run{
                setOnClickListener {
                    setBackgroundResource(R.drawable.background_subscribe_button)
                    typeface = selectFont
                    setTextColor(Color.WHITE)
                    orderDeliverBtnFirst.setBackgroundResource(R.drawable.address_not_select_backround)
                    orderDeliverBtnFirst.setTextColor(buyActivity.getColor(R.color.lstButtonTextGrayColor))
                    orderDeliverBtnFirst.typeface = nomalFont
                    orderDeliverBtnSecond.setBackgroundResource(R.drawable.address_not_select_backround)
                    orderDeliverBtnSecond.setTextColor(buyActivity.getColor(R.color.lstButtonTextGrayColor))
                    orderDeliverBtnSecond.typeface = nomalFont
                    selAddressIdx = 2
                    orderUserViewModel.lookOrderUserAddress(selAddressIdx)
                }
            }

            orderDeliverBtnSave.run{
                setOnClickListener {
                    orderUserViewModel.addressData.value?.receiver = orderDeliverEditName.text.toString()
                    orderUserViewModel.addressData.value?.receiverPhoneNum = orderDeliverEditPhone.text.toString()
                    orderUserViewModel.addressData.value?.address = "${orderDeliverEditAddr.text}/${orderDeliverEditAddrDetail.text}"
                    orderUserViewModel.addressData.value?.context = orderDeliverMemoEditText.text.toString()
                    orderUserViewModel.orderUserAddressList.value!![selAddressIdx] =
                        orderUserViewModel.addressData.value!!
                    orderUserViewModel.setOrderUserAddress(selAddressIdx)
                }
            }

            orderDeliverBtnMemoSelect.run{
                setOnClickListener{
                    orderDeliverMemoEditText.isVisible = !orderDeliverMemoEditText.isVisible
                    if(orderDeliverMemoEditText.isVisible){
                        softInputVisible(orderDeliverMemoEditText, true)
                        orderDeliverMemoVisibleBtn.setImageResource(R.drawable.expand_less_24px)
                    } else {
                        softInputVisible(this, false)
                        orderDeliverMemoVisibleBtn.setImageResource(R.drawable.expand_more_24px)
                    }
                }
            }

            orderDeliverMemoVisibleBtn.run{
                setOnClickListener{
                    orderDeliverMemoEditText.isVisible = !orderDeliverMemoEditText.isVisible
                    if(orderDeliverMemoEditText.isVisible){
                        softInputVisible(orderDeliverMemoEditText, true)
                        orderDeliverMemoVisibleBtn.setImageResource(R.drawable.expand_less_24px)
                    } else {
                        softInputVisible(this, false)
                        orderDeliverMemoVisibleBtn.setImageResource(R.drawable.expand_more_24px)
                    }
                }
            }

            orderDeliverBtnToggle.run{
                setOnClickListener {
                    orderDeliverLayout.isVisible = !orderDeliverLayout.isVisible
                    if(orderDeliverLayout.isVisible){
                        this.setImageResource(R.drawable.expand_less_24px)
                    } else {
                        this.setImageResource(R.drawable.expand_more_24px)
                    }
                }
            }

            orderItemListBtnToggle.run{
                setOnClickListener {
                    orderItemListLayout.isVisible = !orderItemListLayout.isVisible
                    if(orderItemListLayout.isVisible){
                        this.setImageResource(R.drawable.expand_less_24px)
                    } else {
                        this.setImageResource(R.drawable.expand_more_24px)
                    }
                }
            }

            orderPayInfoBtnToggle.run{
                setOnClickListener {
                    orderPayInfoLayout.isVisible = !orderPayInfoLayout.isVisible
                    if(orderPayInfoLayout.isVisible){
                        this.setImageResource(R.drawable.expand_less_24px)
                    } else {
                        this.setImageResource(R.drawable.expand_more_24px)
                    }
                }
            }

            orderRefundInfoBtnToggle.run{
                setOnClickListener {
                    orderRefundInfoDetail.isVisible = !orderRefundInfoDetail.isVisible
                    if(orderRefundInfoDetail.isVisible){
                        this.setImageResource(R.drawable.expand_less_24px)
                    } else {
                        this.setImageResource(R.drawable.expand_more_24px)
                    }
                }
            }

            orderPayInfoBtnCard.run{
                setOnClickListener {
                    orderPayInfoCardLayout.visibility = View.VISIBLE
                    orderPayInfoAccountTransferLayout.visibility = View.GONE
                    orderPayInfoPhoneLayout.visibility = View.GONE

                    setTextColor(buyActivity.getColor(R.color.reviewWriteColor))
                    setStrokeColorResource(R.color.reviewWriteColor)
                    orderPayInfoBtnAccountTransfer.setTextColor(buyActivity.getColor(R.color.lstButtonTextGrayColor))
                    orderPayInfoBtnAccountTransfer.setStrokeColorResource(R.color.lstButtonTextGrayColor)
                    orderPayInfoBtnPhone.setTextColor(buyActivity.getColor(R.color.lstButtonTextGrayColor))
                    orderPayInfoBtnPhone.setStrokeColorResource(R.color.lstButtonTextGrayColor)
                }
            }

            orderPayInfoBtnAccountTransfer.run{
                setOnClickListener {
                    orderPayInfoCardLayout.visibility = View.GONE
                    orderPayInfoAccountTransferLayout.visibility = View.VISIBLE
                    orderPayInfoPhoneLayout.visibility = View.GONE

                    setTextColor(buyActivity.getColor(R.color.reviewWriteColor))
                    setStrokeColorResource(R.color.reviewWriteColor)
                    orderPayInfoBtnCard.setTextColor(buyActivity.getColor(R.color.lstButtonTextGrayColor))
                    orderPayInfoBtnCard.setStrokeColorResource(R.color.lstButtonTextGrayColor)
                    orderPayInfoBtnPhone.setTextColor(buyActivity.getColor(R.color.lstButtonTextGrayColor))
                    orderPayInfoBtnPhone.setStrokeColorResource(R.color.lstButtonTextGrayColor)
                }
            }

            orderPayInfoBtnPhone.run{
                setOnClickListener {
                    orderPayInfoCardLayout.visibility = View.GONE
                    orderPayInfoAccountTransferLayout.visibility = View.GONE
                    orderPayInfoPhoneLayout.visibility = View.VISIBLE

                    setTextColor(buyActivity.getColor(R.color.reviewWriteColor))
                    setStrokeColorResource(R.color.reviewWriteColor)
                    orderPayInfoBtnCard.setTextColor(buyActivity.getColor(R.color.lstButtonTextGrayColor))
                    orderPayInfoBtnCard.setStrokeColorResource(R.color.lstButtonTextGrayColor)
                    orderPayInfoBtnAccountTransfer.setTextColor(buyActivity.getColor(R.color.lstButtonTextGrayColor))
                    orderPayInfoBtnAccountTransfer.setStrokeColorResource(R.color.lstButtonTextGrayColor)
                }
            }

            orderPayInfoPhoneBtn.run{
                setOnClickListener {
                    val builder = MaterialAlertDialogBuilder(buyActivity)
                    builder.setTitle("휴대폰 결제")
                    builder.setMessage("본인 인증 회원만 휴대폰 결제가 가능 합니다.")
                    builder.setNegativeButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                        if(orderUserViewModel.verify.value!!){
                            Snackbar.make(fragmentOrderBinding.root, "휴대폰 결제 설정 되었습니다.", Snackbar.LENGTH_SHORT).show()
                        } else{
                            Snackbar.make(fragmentOrderBinding.root, "본인 인증된 회원만 가능합니다.", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    builder.setPositiveButton("취소", null)
                    builder.show()
                }
            }



        }
    }

    private fun softInputVisible(view:View, visible: Boolean){
        if(visible){
            view.requestFocus()
            val inputMethodManger = buyActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            thread {
                SystemClock.sleep(200)
                inputMethodManger.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }else {
            val inputMethodManager = buyActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            view.clearFocus()
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun viewModelSetting(){
        orderUserViewModel = ViewModelProvider(buyActivity)[OrderUserViewModel::class.java]
        orderItemViewModel = ViewModelProvider(buyActivity)[OrderItemViewModel::class.java]

        orderItemViewModel.run{
            productMap.observe(buyActivity){
                totalOrderProductCount = 0
                totalOrderProductPrice = 0
                productViewSetting(it)
            }

            orderItemList.forEach {
                getOrderProductData(it)
            }
        }

        orderUserViewModel.run{
            verify.observe(buyActivity){
                if(it){
                    fragmentOrderBinding.orderUserBtnAuth.visibility = View.GONE
                    if(!authCheck){
                        Snackbar.make(fragmentOrderBinding.root, "본인 인증 완료되었습니다.", Snackbar.LENGTH_SHORT).show()
                        authCheck = true
                    }
                } else {
                    fragmentOrderBinding.orderUserBtnAuth.visibility = View.VISIBLE
                    fragmentOrderBinding.orderUserLayoutAuthComplete.visibility = View.GONE
                    authCheck = false
                }
            }

            nickname.observe(buyActivity){
                fragmentOrderBinding.orderUserName.text = it
            }

            phoneNum.observe(buyActivity){
                fragmentOrderBinding.orderUserPhoneNumberTextView.text = it
            }

            addressData.observe(buyActivity){
                fragmentOrderBinding.orderDeliverEditName.setText(it.receiver)
                fragmentOrderBinding.orderDeliverEditPhone.setText(it.receiverPhoneNum)
                fragmentOrderBinding.orderDeliverEditAddr.setText(it.address.split("/")[0])
                fragmentOrderBinding.orderDeliverEditAddrDetail.setText(it.address.split("/")[1])
                fragmentOrderBinding.orderDeliverMemoEditText.setText(it.context)
            }

            orderUserCouponList.observe(buyActivity){
                userCouponList = it
                Log.d("tttt", "$it")
            }

            orderUserPossibleCouponList.observe(buyActivity){
                possibleCouponList = it
                Log.d("tttt", "$it")
            }

            getOdderUserAddress(orderUserIdx,0)
            orderUserAuthCheck(orderUserIdx)
            getOrderUserCoupon(orderUserIdx)
            for(coupon in userCouponList){
                getOrderUserPossibleCoupon(coupon.couponIdx)
            }
        }
    }

    private fun productViewSetting(map: LinkedHashMap<String, OrderProduct>){
        orderProductList.clear()
        fragmentOrderBinding.orderItemListLayout.removeAllViews()
        for(itemIdx in orderItemList){
            if(map[itemIdx] != null) {
                orderProductList.add(map[itemIdx]!!)
                getTotalCount(1, true)
                getTotalPrice(map[itemIdx]!!.price, true)

                rowOrderItemListBinding = RowOrderItemListBinding.inflate(layoutInflater)
                rowOrderItemListBinding.run{
                    rowOrderItemListName.text = map[itemIdx]!!.name
                    var oriCount = rowOrderItemListCount.text.toString().toInt()
                    rowOrderItemListPrice.text = changeWon(map[itemIdx]!!.price, 1)
                    if(map[itemIdx]!!.img != null){
                        rowOrderItemListImg.setImageBitmap(map[itemIdx]!!.img)
                    } else {
                        rowOrderItemListImg.setImageResource(R.drawable.product_sample)
                    }
                    rowOrderItemListBtnPlus.setOnClickListener {
                        oriCount++
                        rowOrderItemListCount.text = oriCount.toString()
                        getTotalPrice(map[itemIdx]!!.price, true)
                        getTotalCount(1, true)
                        rowOrderItemListPrice.text = changeWon(map[itemIdx]!!.price, oriCount)
                    }
                    rowOrderItemListBtnMinus.setOnClickListener {
                        if(oriCount > 1) {
                            oriCount--
                            getTotalPrice(map[itemIdx]!!.price, false)
                            getTotalCount(1, false)
                            rowOrderItemListPrice.text = changeWon(map[itemIdx]!!.price, oriCount)
                        }
                        rowOrderItemListCount.text = oriCount.toString()
                    }
                    rowOrderItemListBtnCoupon.setOnClickListener {

                    }
                }
                fragmentOrderBinding.orderItemListLayout.addView(rowOrderItemListBinding.root)
                fragmentOrderBinding.orderPayBtnCount.text = "Total $totalOrderProductCount Items"
            }
        }
    }

    private fun viewSetting(){
        fragmentOrderBinding.run{
            orderPayInfoCardListBrand.run{
                val a1 = ArrayAdapter<String>(buyActivity, android.R.layout.simple_list_item_1, cardList)
                a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapter = a1
                setSelection(0)
            }

            orderPayInfoCardListMonths.run{
                val a1 = ArrayAdapter<String>(buyActivity, android.R.layout.simple_list_item_1, installmentMonth)
                a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapter = a1
                setSelection(0)
            }

            orderPayInfoAccountTransferList.run{
                val a1 = ArrayAdapter<String>(buyActivity, android.R.layout.simple_list_item_1, cardList)
                a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapter = a1
                setSelection(0)
            }
        }
    }

    private fun getTotalPrice(price: String, plus: Boolean){
        val sb = StringBuilder()
        val sumPrice = if(plus) totalOrderProductPrice + price.toInt() else totalOrderProductPrice - price.toInt()
        totalOrderProductPrice = sumPrice

        if(sumPrice < 0) return

        sumPrice.toString().reversed().forEachIndexed { index, c ->
            sb.append("$c")
            if((index+1) % 3 == 0)sb.append(",")
        }

        if(sb.last() == ',') sb.deleteCharAt(sb.lastIndex)

        fragmentOrderBinding.orderPayBtnTotal.text = "${sb.reverse()}원"
    }

    private fun getTotalCount(num: Int, plus: Boolean){
        totalOrderProductCount = if(plus) totalOrderProductCount + num else totalOrderProductCount - num
        fragmentOrderBinding.orderPayBtnCount.text = "Total $totalOrderProductCount Items"
    }

    private fun changeWon(price: String, count: Int): String{
        val sb = StringBuilder()
        val sumPrice = price.replace(",","").replace("원","").toInt() * count

        sumPrice.toString().reversed().forEachIndexed { index, c ->
            sb.append("$c")
            if((index+1) % 3 == 0)sb.append(",")
        }

        if(sb.last() == ',') sb.deleteCharAt(sb.lastIndex)

        return "${sb.reverse()}원"
    }
}