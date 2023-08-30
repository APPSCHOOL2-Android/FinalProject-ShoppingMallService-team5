package com.hifi.hifi_shopping.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.databinding.FragmentCouponEnabledBinding
import com.hifi.hifi_shopping.databinding.RowCouponEnabledBinding
import com.hifi.hifi_shopping.user.model.ProductDataClass
import com.hifi.hifi_shopping.user.vm.ProductViewModel
import com.hifi.hifi_shopping.user.vm.UserCouponViewModel

class CouponEnabledFragment : Fragment() {
    lateinit var fragmentCouponEnabledBinding: FragmentCouponEnabledBinding
    lateinit var userActivity: UserActivity
    lateinit var userCouponViewModel:UserCouponViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userActivity = activity as UserActivity
        fragmentCouponEnabledBinding = FragmentCouponEnabledBinding.inflate(layoutInflater)
        userCouponViewModel = ViewModelProvider(userActivity)[UserCouponViewModel::class.java]

        val userTemp = userActivity.userTemp

        userCouponViewModel.run {
            getUserCouponEnabledAll(userTemp.idx)
            couponEnabledList.observe(userActivity){
                fragmentCouponEnabledBinding.run {
                    couponEnabledRecyclerView.run {
                        adapter = CouponEnabledRecyclerViewAdapter()
                        layoutManager = LinearLayoutManager(userActivity, LinearLayoutManager.VERTICAL, false)
                    }
                }
            }
        }

        return fragmentCouponEnabledBinding.root
    }

    inner class CouponEnabledRecyclerViewAdapter() : RecyclerView.Adapter<CouponEnabledRecyclerViewAdapter.CouponEnabledRecyclerViewHolder>(){
        inner class CouponEnabledRecyclerViewHolder(rowCouponEnabledBinding: RowCouponEnabledBinding) : RecyclerView.ViewHolder(rowCouponEnabledBinding.root){

            val rowCouponEnabledName: TextView
            val rowCouponEnabledEndDate: TextView
            val rowCouponEnabledDiscount: TextView


            init{
                rowCouponEnabledName = rowCouponEnabledBinding.rowCouponEnabledCategory
                rowCouponEnabledEndDate = rowCouponEnabledBinding.rowCouponEnabledValidDate
                rowCouponEnabledDiscount = rowCouponEnabledBinding.rowCouponEnabledDiscount
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponEnabledRecyclerViewHolder {
            val rowCouponEnabledBinding = RowCouponEnabledBinding.inflate(layoutInflater)
            val allViewHolder = CouponEnabledRecyclerViewHolder(rowCouponEnabledBinding)

            rowCouponEnabledBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return allViewHolder
        }

        override fun getItemCount(): Int {
            return userCouponViewModel.couponEnabledList.value?.size!!
        }

        override fun onBindViewHolder(holder: CouponEnabledRecyclerViewHolder, position: Int) {
            holder.rowCouponEnabledName.text = userCouponViewModel.couponEnabledList.value?.get(position)?.categoryNum
//            holder.rowCouponEnabledStartDate.text =
            holder.rowCouponEnabledEndDate.text = "~"+userCouponViewModel.couponEnabledList.value?.get(position)?.validDate
            holder.rowCouponEnabledDiscount.text = userCouponViewModel.couponEnabledList.value?.get(position)?.discountPercent


        }
    }



}