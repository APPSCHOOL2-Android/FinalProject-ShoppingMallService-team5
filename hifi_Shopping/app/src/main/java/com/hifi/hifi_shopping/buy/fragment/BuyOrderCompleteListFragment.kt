package com.hifi.hifi_shopping.buy.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.databinding.FragmentBuyOrderCompleteListBinding


class BuyOrderCompleteListFragment : Fragment() {

    lateinit var fragmentBuyOrderCompleteListBinding: FragmentBuyOrderCompleteListBinding
    lateinit var buyActivity: BuyActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentBuyOrderCompleteListBinding = FragmentBuyOrderCompleteListBinding.inflate(layoutInflater)
        buyActivity = activity as BuyActivity

        productInfoSetting()

        return fragmentBuyOrderCompleteListBinding.root
    }
    private fun productInfoSetting(){
        buyActivity.orderCompleteProductList.forEach { rowOrderItemListBinding ->
            val parent = rowOrderItemListBinding.root.parent as ViewGroup?
            parent?.removeView(rowOrderItemListBinding.root)

            rowOrderItemListBinding.run{
                rowOrderItemListBtnMinus.visibility = View.GONE
                rowOrderItemListBtnPlus.visibility = View.GONE
                rowOrderItemListBtnCoupon.visibility = View.GONE
                rowOrderItemListPrice.visibility = View.GONE
                rowOrderItemDeliverPriceInfo.visibility = View.GONE


                rowOrderItemListName.setTextColor(buyActivity.getColor(R.color.black))
                rowOrderItemListName.textSize = 14f

                rowOrderItemListCount.text = "수량: ${rowOrderItemListCount.text}개"
                rowOrderItemListCount.setTextColor(buyActivity.getColor(R.color.black))
                rowOrderItemListCount.textSize = 12.5f

                rowOrderItemListDeliverPrice.text = "₩ ${rowOrderItemListPrice.text}"
                rowOrderItemListDeliverPrice.setTextColor(buyActivity.getColor(R.color.dark_brown))
                rowOrderItemListDeliverPrice.textSize = 12.5f
            }

            fragmentBuyOrderCompleteListBinding.layoutBuyOrderCompleteProduct
                .addView(rowOrderItemListBinding.root)
        }

    }

}