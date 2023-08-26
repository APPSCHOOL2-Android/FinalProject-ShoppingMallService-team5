package com.hifi.hifi_shopping.buy.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.auth.AuthActivity
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.buy.buy_repository.OrderItemRepository
import com.hifi.hifi_shopping.buy.buy_vm.OrderItemViewModel
import com.hifi.hifi_shopping.buy.buy_vm.OrderUserViewModel
import com.hifi.hifi_shopping.buy.datamodel.OrderData
import com.hifi.hifi_shopping.buy.datamodel.OrderProduct
import com.hifi.hifi_shopping.buy.datamodel.OrderUserCoupon
import com.hifi.hifi_shopping.buy.datamodel.PossibleCoupon
import com.hifi.hifi_shopping.buy.datamodel.categoryData
import com.hifi.hifi_shopping.databinding.FragmentOrderBinding
import com.hifi.hifi_shopping.databinding.ItemByCouponListBinding
import com.hifi.hifi_shopping.databinding.RowOrderItemListBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


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
    private lateinit var itemByCouponListBinding: ItemByCouponListBinding

    private var authCheck = true

    private val cardList = arrayOf(
        "은행 선택","신한", "농협", "국민", "우리", "카카오", "토스"
    )
    private val installmentMonth = arrayOf(
        "일시불", "2개월", "3개월", "4개월", "5개월", "6개월", "12개월", "24개월", "36개월"
    )

    private var userCouponList = mutableListOf<OrderUserCoupon>()
    private var possibleCouponList = mutableListOf<PossibleCoupon>()

    lateinit var dialog: AlertDialog
    private var itemByCoupon = LinkedHashMap<String, PossibleCoupon>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        basicDataSetting()
        viewModelSetting()
        viewSetting()
        clickEventSetting()

        return fragmentOrderBinding.root
    }

    private fun basicDataSetting(){
        fragmentOrderBinding = FragmentOrderBinding.inflate(layoutInflater)
        buyActivity = activity as BuyActivity
        orderUserIdx = arguments?.getString("userIdx")!!
        orderItemList = arguments?.getStringArrayList("selProduct")!!
        fragmentOrderBinding.orderPayBtnCount.text = "Total ${orderProductList.size} Items"
        itemByCoupon.clear()
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
                        buyActivity.softInputVisible(orderDeliverMemoEditText, true)
                        orderDeliverMemoVisibleBtn.setImageResource(R.drawable.expand_less_24px)
                    } else {
                        buyActivity.softInputVisible(this, false)
                        orderDeliverMemoVisibleBtn.setImageResource(R.drawable.expand_more_24px)
                    }
                }
            }

            orderDeliverMemoVisibleBtn.run{
                setOnClickListener{
                    orderDeliverMemoEditText.isVisible = !orderDeliverMemoEditText.isVisible
                    if(orderDeliverMemoEditText.isVisible){
                        buyActivity.softInputVisible(orderDeliverMemoEditText, true)
                        orderDeliverMemoVisibleBtn.setImageResource(R.drawable.expand_less_24px)
                    } else {
                        buyActivity.

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

            orderPayBtn.run{
                setOnClickListener {

                    if(orderDeliverEditName.text.isNullOrEmpty() || orderDeliverEditPhone.text.isNullOrEmpty() ||
                        orderDeliverEditAddr.text.isNullOrEmpty() || orderDeliverEditAddrDetail.text.isNullOrEmpty()){
                        Snackbar.make(fragmentOrderBinding.root, "배송지 정보를 입력 하십시오.", Snackbar.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if(orderUserViewModel.addressData.value == null){
                        Snackbar.make(fragmentOrderBinding.root, "배송지 정보를 입력 후 저장하십시오.", Snackbar.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if(!orderUserViewModel.verify.value!!){
                        Snackbar.make(fragmentOrderBinding.root, "본인 인증 회원만 구매가 가능 합니다.", Snackbar.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    buyActivity.orderCompleteAddress = orderUserViewModel.addressData.value!!

                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val writeDate = sdf.format(Date(System.currentTimeMillis()))

                    buyActivity.orderCompleteProductList.forEach { product ->
                        val itemIdx = product.rowOrderItemListName.tag as String
                        repeat(product.rowOrderItemListCount.text.toString().toInt()){
                            val orderData = OrderData(
                                UUID.randomUUID().toString(), // 주문 인덱스
                                orderUserIdx, // 주문자 인덱스
                                itemIdx, // 제품 인덱스
                                if(!itemByCoupon.containsKey(itemIdx)) "null" else itemByCoupon[itemIdx]!!.idx, // 적용 쿠폰 인덱스
                                buyActivity.orderCompleteAddress.idx, // 배송지 인덱스
                                writeDate, // 구매날짜
                                product.rowOrderItemListPrice.text.toString().replace(",","").replace("원",""), // 결제 가격
                                "PACKING" // 배송 상태
                            )
                            OrderItemRepository.setOrderData(orderData)
                        }
                    }
                    buyActivity.removeFragment(BuyActivity.ORDER_FRAGMENT)
                    buyActivity.replaceFragment(BuyActivity.BUY_ORDER_COMPLETE_FRAGMENT, false, null)
                }
            }
        }
    }

    private fun viewModelSetting(){
        orderUserViewModel = ViewModelProvider(buyActivity)[OrderUserViewModel::class.java]
        orderItemViewModel = ViewModelProvider(buyActivity)[OrderItemViewModel::class.java]

        orderItemViewModel.run{
            productMap.observe(buyActivity){
                buyActivity.totalOrderProductCount = 0
                buyActivity.totalOrderProductPrice = 0
                buyActivity.oriTotalOrderProductPrice = 0
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
            }

            orderUserPossibleCouponList.observe(buyActivity){
                possibleCouponList = it
            }

            getOdderUserAddress(orderUserIdx,0)
            orderUserAuthCheck(orderUserIdx)
            getOrderUserCoupon(orderUserIdx)
        }
    }

    private fun productViewSetting(map: LinkedHashMap<String, OrderProduct>){
        orderProductList.clear()
        fragmentOrderBinding.orderItemListLayout.removeAllViews()
        buyActivity.orderCompleteProductList.clear()

        for(itemIdx in orderItemList){

            if(map[itemIdx] != null) {

                orderProductList.add(map[itemIdx]!!)
                getTotalCount(1, true)
                var buyingPrice = map[itemIdx]!!.price
                if(itemByCoupon.containsKey(itemIdx)){ // 쿠폰 적용에 따른 가격
                    val discount = itemByCoupon[itemIdx]!!.discountPercent
                    buyingPrice = ((map[itemIdx]!!.price.toInt() * discount.toInt()) / 100).toString()
                }
                getTotalPrice(buyingPrice, true) // 제품가격
                getTotalNotDiscountPrice(map[itemIdx]!!.price, true)
                getTotalPrice("3000", true) // 배송비

                val itemCouponList = possibleCouponList.filter {
                    it.categoryNum == map[itemIdx]!!.category.slice(0 until it.categoryNum.length)
                }

                rowOrderItemListBinding = RowOrderItemListBinding.inflate(layoutInflater)

                rowOrderItemListBinding.run{
                    rowOrderItemListName.text = map[itemIdx]!!.name
                    rowOrderItemListName.tag = "$itemIdx"

                    var oriCount = rowOrderItemListCount.text.toString().toInt()
                    rowOrderItemListPrice.text = buyActivity.changeWon(buyingPrice, 1)

                    if(map[itemIdx]!!.img != null){
                        rowOrderItemListImg.setImageBitmap(map[itemIdx]!!.img)
                    } else {
                        rowOrderItemListImg.setImageResource(R.drawable.product_sample)
                    }

                    rowOrderItemListBtnPlus.setOnClickListener {
                        oriCount++
                        rowOrderItemListCount.text = oriCount.toString()
                        getTotalPrice(buyingPrice, true)
                        getTotalNotDiscountPrice(map[itemIdx]!!.price, true)
                        getTotalCount(1, true)
                        rowOrderItemListPrice.text = buyActivity.changeWon(buyingPrice, oriCount)
                    }

                    rowOrderItemListBtnMinus.setOnClickListener {
                        if(oriCount > 1) {
                            oriCount--
                            getTotalPrice(buyingPrice, false)
                            getTotalNotDiscountPrice(map[itemIdx]!!.price, false)
                            getTotalCount(1, false)
                            rowOrderItemListPrice.text = buyActivity.changeWon(buyingPrice, oriCount)
                        }
                        rowOrderItemListCount.text = oriCount.toString()
                    }

                    rowOrderItemListBtnCoupon.run{
                        if(itemCouponList.isEmpty()){
                            visibility = View.GONE
                        }
                        setOnClickListener {

                            val builder = AlertDialog.Builder(buyActivity)

                            builder.setTitle("적용 가능 쿠폰 리스트")
                            builder.setMessage("선택 하였을 경우 변경 및 취소가 불가능 합니다.\n그래도 적용 하시겠습니까?")

                            builder.setPositiveButton("취소"){ dialogInterface: DialogInterface, i: Int ->
                                Snackbar.make(fragmentOrderBinding.root, "쿠폰 적용을 취소 하였습니다.", Snackbar.LENGTH_SHORT).show()
                            }
                            itemByCouponListBinding = ItemByCouponListBinding.inflate(layoutInflater)

                            itemCouponList.forEach { coupon ->
                                val textView = TextView(buyActivity)
                                textView.run {
                                    val key = coupon.categoryNum.slice(0 until coupon.categoryNum.length-1).toInt()
                                    val value = coupon.categoryNum.last() - '0' - 1
                                    val categoryName = categoryData[key]?.get(value)

                                    text = "$categoryName 카테고리 ${coupon.discountPercent}% 할인 쿠폰 적용"
                                    textSize = 16f
                                    gravity = android.view.Gravity.CENTER
                                    setBackgroundColor(Color.WHITE)
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )

                                    setOnClickListener {
                                        orderUserViewModel.setOrderUserCoupon(coupon.idx, orderUserIdx)
                                        orderItemList.forEach {
                                            orderItemViewModel.getOrderProductData(it)
                                        }
                                        itemByCoupon[itemIdx] = coupon
                                        dialog.dismiss()
                                        Snackbar.make(fragmentOrderBinding.root, "${textView.text} 하였습니다.", Snackbar.LENGTH_SHORT).show()
                                    }
                                }
                                itemByCouponListBinding.root.addView(textView)
                                itemByCouponListBinding.root.gravity = android.view.Gravity.CENTER
                            }
                            builder.setView(itemByCouponListBinding.root)
                            dialog = builder.show()
                        }
                    }
                }
                fragmentOrderBinding.orderItemListLayout.addView(rowOrderItemListBinding.root)
                buyActivity.orderCompleteProductList.add(rowOrderItemListBinding)
                fragmentOrderBinding.orderPayBtnCount.text = "Total ${buyActivity.totalOrderProductCount} Items"

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
        val sumPrice = if(plus) buyActivity.totalOrderProductPrice + price.toInt() else buyActivity.totalOrderProductPrice - price.toInt()
        buyActivity.totalOrderProductPrice = sumPrice

        if(sumPrice < 0) return

        sumPrice.toString().reversed().forEachIndexed { index, c ->
            sb.append("$c")
            if((index+1) % 3 == 0)sb.append(",")
        }

        if(sb.last() == ',') sb.deleteCharAt(sb.lastIndex)

        fragmentOrderBinding.orderPayBtnTotal.text = "${sb.reverse()}원"
    }

    private fun getTotalNotDiscountPrice(price: String, plus: Boolean){
        val sb = StringBuilder()
        val sumPrice = if(plus) buyActivity.oriTotalOrderProductPrice + price.toInt() else buyActivity.oriTotalOrderProductPrice - price.toInt()
        buyActivity.oriTotalOrderProductPrice = sumPrice

    }

    private fun getTotalCount(num: Int, plus: Boolean){
        buyActivity.totalOrderProductCount = if(plus) buyActivity.totalOrderProductCount + num else buyActivity.totalOrderProductCount - num
        fragmentOrderBinding.orderPayBtnCount.text = "Total ${buyActivity.totalOrderProductCount} Items"
    }

}