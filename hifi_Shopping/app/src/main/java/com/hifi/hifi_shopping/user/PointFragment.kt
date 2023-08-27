package com.hifi.hifi_shopping.user

import android.content.Intent
import android.graphics.BitmapFactory
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
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.databinding.FragmentPointBinding
import com.hifi.hifi_shopping.databinding.RowPointBinding
import com.hifi.hifi_shopping.user.repository.ProductImgRepository
import com.hifi.hifi_shopping.user.vm.OrderViewModel
import com.hifi.hifi_shopping.user.vm.PointViewModel
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class PointFragment : Fragment() {
    lateinit var fragmentPointBinding: FragmentPointBinding
    lateinit var userActivity: UserActivity
    lateinit var pointViewModel: PointViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPointBinding = FragmentPointBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity
        pointViewModel = ViewModelProvider(userActivity)[PointViewModel::class.java]
        val userTemp = userActivity.userTemp
        pointViewModel.run {
            getPointListByUser(userTemp.idx)
            pointDataList.observe(userActivity){
                fragmentPointBinding.run {
                    pointUserPoint.text = pointDataList.value?.map { it.amount.toInt() }?.sum().toString() + "P"
                    pointDetailRecyclerView.run {
                        adapter = PointRecyclerViewAdapter()
                        layoutManager = LinearLayoutManager(userActivity, LinearLayoutManager.VERTICAL, false)
                    }
                }
            }
        }

        
        return fragmentPointBinding.root
    }

    inner class PointRecyclerViewAdapter() : RecyclerView.Adapter<PointRecyclerViewAdapter.PointRecyclerViewHolder>(){

        inner class PointRecyclerViewHolder(rowPointBinding: RowPointBinding) : RecyclerView.ViewHolder(rowPointBinding.root){

            val rowPointDate: TextView
            val rowPointContext: TextView
            val rowPointAmount: TextView


            init{
                rowPointDate = rowPointBinding.rowPointDate
                rowPointContext = rowPointBinding.rowPointContext
                rowPointAmount = rowPointBinding.rowPointAmount
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointRecyclerViewHolder {
            val rowPointBinding = RowPointBinding.inflate(layoutInflater)
            val allViewHolder = PointRecyclerViewHolder(rowPointBinding)

            rowPointBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return allViewHolder
        }

        override fun getItemCount(): Int {
            return pointViewModel.pointDataList.value?.size!!
        }

        override fun onBindViewHolder(holder: PointRecyclerViewHolder, position: Int) {
            holder.rowPointDate.text = pointViewModel.pointDataList.value?.get(position)?.date
            holder.rowPointContext.text = pointViewModel.pointDataList.value?.get(position)?.context
            holder.rowPointAmount.text = pointViewModel.pointDataList.value?.get(position)?.amount+"P"

        }
    }
}