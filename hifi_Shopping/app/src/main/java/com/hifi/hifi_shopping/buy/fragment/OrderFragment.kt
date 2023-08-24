package com.hifi.hifi_shopping.buy.fragment

import android.content.ClipData.Item
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.auth.AuthActivity
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.buy.buy_vm.OrderItemViewModel
import com.hifi.hifi_shopping.buy.buy_vm.OrderProduct
import com.hifi.hifi_shopping.buy.buy_vm.OrderUserViewModel
import com.hifi.hifi_shopping.databinding.FragmentOrderBinding
import com.hifi.hifi_shopping.databinding.RowOrderItemListBinding



class OrderFragment : Fragment() {

    private lateinit var fragmentOrderBinding: FragmentOrderBinding
    private lateinit var buyActivity: BuyActivity

    private lateinit var orderUserViewModel: OrderUserViewModel
    private lateinit var orderItemViewModel: OrderItemViewModel
    private var orderUserIdx = ""
    private var selAddressIdx = 0
    private lateinit var orderItemList : ArrayList<String>
    private var orderProductList = mutableListOf<OrderProduct>()

    private var totalOrderProductCount = 0
    private var totalOrderProductPrice = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        dataSetting()
        viewModelSetting()
        clickEventSetting()


        fragmentOrderBinding.run{
            orderItemListRecyclerView.run{
                adapter = ItemListAdapter()
                layoutManager = LinearLayoutManager(buyActivity)
            }
        }


        return fragmentOrderBinding.root
    }

    inner class ItemListAdapter(): RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {
        inner class ItemViewHolder(rowOrderItemListBinding: RowOrderItemListBinding): ViewHolder(rowOrderItemListBinding.root){
            var rowOrderItemListImg = rowOrderItemListBinding.rowOrderItemListImg
            var rowOrderItemListName = rowOrderItemListBinding.rowOrderItemListName
            var rowOrderItemListPrice = rowOrderItemListBinding.rowOrderItemListPrice
            var rowOrderItemListBtnMinus = rowOrderItemListBinding.rowOrderItemListBtnMinus
            var rowOrderItemListCount = rowOrderItemListBinding.rowOrderItemListCount
            var rowOrderItemListBtnPlus = rowOrderItemListBinding.rowOrderItemListBtnPlus
            var rowOrderItemListBtnCoupon = rowOrderItemListBinding.rowOrderItemListBtnCoupon
            var rowOrderItemListDiscountPrice = rowOrderItemListBinding.rowOrderItemListDiscountPrice

            init{
                rowOrderItemListBinding.run{
                    rowOrderItemListBtnMinus.setOnClickListener {
                        var oriCount = rowOrderItemListCount.text.toString().toInt()
                        if(oriCount > 1) {
                            oriCount--
                            getTotalPrice(rowOrderItemListPrice.text.toString(), false)
                            getTotalCount(1, false)
                        }
                        rowOrderItemListCount.text = oriCount.toString()
                    }
                }
                rowOrderItemListBinding.run{
                    rowOrderItemListBtnPlus.setOnClickListener {
                        var oriCount = rowOrderItemListCount.text.toString().toInt()
                        oriCount++
                        rowOrderItemListCount.text = oriCount.toString()
                        getTotalPrice(rowOrderItemListPrice.text.toString(), true)
                        getTotalCount(1, true)
                    }
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            var rowOrderItemListBinding = RowOrderItemListBinding.inflate(layoutInflater)
            var itemViewHolder = ItemViewHolder(rowOrderItemListBinding)

            rowOrderItemListBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return itemViewHolder
        }

        override fun getItemCount(): Int {
            return orderProductList.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.rowOrderItemListName.text = orderProductList[position].name
            holder.rowOrderItemListPrice.text = orderProductList[position].price
            if(orderProductList[position].img != null){
                holder.rowOrderItemListImg.setImageBitmap(orderProductList[position].img)
            }
        }
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

        }
    }

    private fun viewModelSetting(){
        orderUserViewModel = ViewModelProvider(buyActivity)[OrderUserViewModel::class.java]
        orderItemViewModel = ViewModelProvider(buyActivity)[OrderItemViewModel::class.java]

        orderItemViewModel.run{
            productMap.observe(buyActivity){
                orderProductList.clear()
                totalOrderProductCount = 0
                totalOrderProductPrice = 0
                for(itemIdx in orderItemList){
                    if(it[itemIdx] != null) {
                        orderProductList.add(it[itemIdx]!!)
                        getTotalCount(1, true)
                        getTotalPrice(it[itemIdx]!!.price, true )
                    }
                }
                fragmentOrderBinding.orderItemListRecyclerView.adapter?.notifyDataSetChanged()
                fragmentOrderBinding.orderPayBtnCount.text = "Total $totalOrderProductCount Items"
            }

            orderItemList.forEach {
                getOrderProductData(it)
            }
        }

        orderUserViewModel.run{
            verify.observe(buyActivity){
                if(it){
                    fragmentOrderBinding.orderUserBtnAuth.visibility = View.GONE
                } else {
                    fragmentOrderBinding.orderUserBtnAuth.visibility = View.VISIBLE
                    fragmentOrderBinding.orderUserLayoutAuthComplete.visibility = View.GONE
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

            getOdderUserAddress(orderUserIdx,0)
            orderUserAuthCheck(orderUserIdx)
        }
    }

    fun getTotalPrice(Price: String, plus: Boolean){
        val sb = StringBuilder()
        val sumPrice = if(plus) totalOrderProductPrice + Price.toInt() else totalOrderProductPrice - Price.toInt()
        totalOrderProductPrice = sumPrice
        if(sumPrice < 0) return
        sumPrice.toString().reversed().forEachIndexed { index, c ->
            sb.append("$c")
            if((index+1) % 3 == 0)sb.append(",")
        }
        if(sb.last() == ',') sb.deleteCharAt(sb.lastIndex)
        fragmentOrderBinding.orderPayBtnTotal.text = "${sb.reverse()}원"
    }

    fun getTotalCount(num: Int, plus: Boolean){
        totalOrderProductCount = if(plus) totalOrderProductCount + num else totalOrderProductCount - num
        fragmentOrderBinding.orderPayBtnCount.text = "Total $totalOrderProductCount Items"
    }
}