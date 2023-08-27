package com.hifi.hifi_shopping.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentPointBinding
import com.hifi.hifi_shopping.databinding.RowPointBinding
import com.hifi.hifi_shopping.user.vm.PointViewModel

class PointFragment : Fragment() {
    lateinit var fragmentPointBinding: FragmentPointBinding
    lateinit var userActivity: UserActivity
    lateinit var pointViewModel: PointViewModel
    var isToggled = false
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
            pointDataList.observe(userActivity) {
                fragmentPointBinding.run {
                    pointUserPoint.text =
                        pointDataList.value?.map { it.amount.toInt() }?.sum().toString() + "P"
                    pointDetailRecyclerView.run {
                        adapter = PointRecyclerViewAdapter()
                        layoutManager =
                            LinearLayoutManager(userActivity, LinearLayoutManager.VERTICAL, false)
                    }
                }
            }
        }

        fragmentPointBinding.run {
            pointGuideBtnToggle.setOnClickListener {
                toggleButtonClick(pointGuideBtnToggle,pointGuideTextView)
            }
            pointReviewBtnToggle.setOnClickListener {
                toggleButtonClick(pointReviewBtnToggle,pointReviewLayout)
            }
        }


        return fragmentPointBinding.root
    }

    inner class PointRecyclerViewAdapter() :
        RecyclerView.Adapter<PointRecyclerViewAdapter.PointRecyclerViewHolder>() {

        inner class PointRecyclerViewHolder(rowPointBinding: RowPointBinding) :
            RecyclerView.ViewHolder(rowPointBinding.root) {

            val rowPointDate: TextView
            val rowPointContext: TextView
            val rowPointAmount: TextView


            init {
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
            holder.rowPointAmount.text =
                pointViewModel.pointDataList.value?.get(position)?.amount + "P"

        }
    }

    fun toggleButtonClick(btn: ImageButton, view: View) {
        isToggled = !isToggled
        if (isToggled) {
            // 토글이 활성화된 경우
            btn.setImageResource(R.drawable.expand_less_24px)
            view.visibility = View.VISIBLE
        } else {
            btn.setImageResource(R.drawable.expand_more_24px)
            view.visibility = View.GONE

        }
    }


}