package com.hifi.hifi_shopping.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.MainActivity
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.databinding.FragmentMyPageBinding
import com.hifi.hifi_shopping.user.model.PointDataClass
import com.hifi.hifi_shopping.user.model.UserDataClass
import com.hifi.hifi_shopping.user.vm.PointViewModel
import com.hifi.hifi_shopping.user.vm.UserCouponViewModel
import java.util.UUID

class MyPageFragment : Fragment() {

    lateinit var fragmentMyPageBinding : FragmentMyPageBinding
    lateinit var userActivity: UserActivity
    lateinit var pointViewModel: PointViewModel
    lateinit var userCouponViewModel: UserCouponViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMyPageBinding = FragmentMyPageBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity


        val userTemp = userActivity.userData
        pointViewModel = ViewModelProvider(userActivity)[PointViewModel::class.java]
        userCouponViewModel = ViewModelProvider(userActivity)[UserCouponViewModel::class.java]


        pointViewModel.run {
            pointDataList.observe(userActivity){
                val pointValue = pointDataList.value?.map { p->p.amount }?.sum()
//                Log.d("포인트테스트",pointValue.toString())
                fragmentMyPageBinding.myPagePointCount.text = pointValue.toString()
//                fragmentPointBinding.recyclerViewPostListResult.adapter?.notifyDataSetChanged()
            }
        }

        userCouponViewModel.run {
            userCouponDataList.observe(userActivity){
                val couponCount = userCouponDataList.value?.filter { c-> c.used }?.size
                fragmentMyPageBinding.myPageCouponCount.text = couponCount.toString()
            }
        }



        fragmentMyPageBinding.run {
            myPageUserNick.text = userTemp.nickname

            myPageToPoint.run {
//                Log.d("포인트",pointSum.toString())
//                myPagePointCount.text = pointSum.toString()
                pointViewModel.getPointList(userTemp.idx)

                setOnClickListener {
                    userActivity.replaceFragment(UserActivity.POINT_FRAGMENT, true, null)
                }

            }
            myPageToCoupon.run {
                userCouponViewModel.getUserCouponAll(userTemp.idx)
                setOnClickListener {
                    userActivity.replaceFragment(UserActivity.COUPON_FRAGMENT, true, null)
                }
            }

            myPageToReview.run {
                setOnClickListener {
                    userActivity.replaceFragment(UserActivity.USER_PAGE_FRAGMENT, true, null)
                }
            }

            // 아이템 추천 받으러 가기




            // 배송 현황
            myPageDeliverStatusPay.run {
                setOnClickListener{

                }
            }


            // 기본 템 보유 현황
            myPageBasicItemStatusHobby.run {
                setOnClickListener{

                }
            }

            myPageBasicItemStatusLiving.run {
                setOnClickListener{

                }
            }

            myPageBasicItemStatusPrepare.run {
                setOnClickListener{

                }
            }

            // 내 구독리스트 확인하러 가기
            myPageBtnToSubscribe.setOnClickListener {

            }
        }

        return fragmentMyPageBinding.root
    }






}