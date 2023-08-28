package com.hifi.hifi_shopping.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifi.hifi_shopping.databinding.FragmentCouponDisabledBinding
import com.hifi.hifi_shopping.databinding.RowCouponDisabledBinding
import com.hifi.hifi_shopping.user.model.CouponDataClass
import com.hifi.hifi_shopping.user.vm.UserCouponViewModel


class CouponDisabledFragment : Fragment() {

    lateinit var userActivity : UserActivity
    lateinit var fragmentCouponDisabledBinding: FragmentCouponDisabledBinding
    lateinit var userCouponViewModel : UserCouponViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userActivity = activity as UserActivity
        fragmentCouponDisabledBinding = FragmentCouponDisabledBinding.inflate(layoutInflater)

        userCouponViewModel = ViewModelProvider(userActivity)[UserCouponViewModel::class.java]

        val userTemp = userActivity.userTemp

        userCouponViewModel.run {
            getUserCouponDisabledAll(userTemp.idx)
            couponDisabledList.observe(userActivity){
                fragmentCouponDisabledBinding.run {
                    couponDisabledRecyclerView.run {
                        adapter = CouponDisabledRecyclerViewAdapter()
                        layoutManager = LinearLayoutManager(userActivity, LinearLayoutManager.VERTICAL, false)
                    }
                }
            }
        }

        return fragmentCouponDisabledBinding.root
    }

    inner class CouponDisabledRecyclerViewAdapter() : RecyclerView.Adapter<CouponDisabledRecyclerViewAdapter.CouponDisabledRecyclerViewHolder>(){

        inner class CouponDisabledRecyclerViewHolder(rowCouponDisabledBinding: RowCouponDisabledBinding) : RecyclerView.ViewHolder(rowCouponDisabledBinding.root){

            val rowCouponDisabledName: TextView
            val rowCouponDisabledEndDate: TextView
            val rowCouponDisabledDiscount: TextView


            init{
                rowCouponDisabledName = rowCouponDisabledBinding.rowCouponDisabledName
                rowCouponDisabledEndDate = rowCouponDisabledBinding.rowCouponDisabledValidDate
                rowCouponDisabledDiscount = rowCouponDisabledBinding.rowCouponDisabledDiscount
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponDisabledRecyclerViewHolder {
            val rowCouponDisabledBinding = RowCouponDisabledBinding.inflate(layoutInflater)
            val allViewHolder = CouponDisabledRecyclerViewHolder(rowCouponDisabledBinding)

            rowCouponDisabledBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return allViewHolder
        }

        override fun getItemCount(): Int {
            return userCouponViewModel.couponDisabledList.value?.size!!
        }

        override fun onBindViewHolder(holder: CouponDisabledRecyclerViewHolder, position: Int) {
            holder.rowCouponDisabledName.text = userCouponViewModel.couponDisabledList.value?.get(position)?.categoryNum

            holder.rowCouponDisabledEndDate.text = "~"+userCouponViewModel.couponDisabledList.value?.get(position)?.validDate
            holder.rowCouponDisabledDiscount.text =userCouponViewModel.couponDisabledList.value?.get(position)?.discountPercent


        }
    }

}