package com.hifi.hifi_shopping.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.hifi.hifi_shopping.R
import com.hifi.hifi_shopping.auth.AuthActivity
import com.hifi.hifi_shopping.buy.BuyActivity
import com.hifi.hifi_shopping.category.CategoryActivity
import com.hifi.hifi_shopping.databinding.FragmentMyPageBinding
import com.hifi.hifi_shopping.parcel.ParcelActivity
import com.hifi.hifi_shopping.recommend.RecommendActivity
import com.hifi.hifi_shopping.search.SearchActivity
import com.hifi.hifi_shopping.subscribe.SubscribeActivity
import com.hifi.hifi_shopping.user.vm.OrderViewModel
import com.hifi.hifi_shopping.user.vm.PointViewModel
import com.hifi.hifi_shopping.user.vm.ProductViewModel
import com.hifi.hifi_shopping.user.vm.ReviewViewModel
import com.hifi.hifi_shopping.user.vm.SubscribeViewModel
import com.hifi.hifi_shopping.user.vm.UserCouponViewModel

class MyPageFragment : Fragment() {

    lateinit var fragmentMyPageBinding : FragmentMyPageBinding
    lateinit var userActivity: UserActivity
    lateinit var pointViewModel: PointViewModel
    lateinit var userCouponViewModel: UserCouponViewModel
    lateinit var reviewViewModel: ReviewViewModel
    lateinit var subscribeViewModel: SubscribeViewModel
    lateinit var orderViewModel: OrderViewModel
    lateinit var productViewModel: ProductViewModel
    val auth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMyPageBinding = FragmentMyPageBinding.inflate(layoutInflater)
        userActivity = activity as UserActivity


        val userTemp = userActivity.userTemp
        pointViewModel = ViewModelProvider(userActivity)[PointViewModel::class.java]
        userCouponViewModel = ViewModelProvider(userActivity)[UserCouponViewModel::class.java]
        reviewViewModel = ViewModelProvider(userActivity)[ReviewViewModel::class.java]
        subscribeViewModel = ViewModelProvider(userActivity)[SubscribeViewModel::class.java]
        orderViewModel = ViewModelProvider(userActivity)[OrderViewModel::class.java]
        productViewModel = ViewModelProvider(userActivity)[ProductViewModel::class.java]


        pointViewModel.run {
            pointDataList.observe(userActivity){
                val pointValue = pointDataList.value?.map { p->p.amount.toInt() }?.sum()
//                Log.d("포인트테스트",pointValue.toString())
                fragmentMyPageBinding.myPagePointCount.text = pointValue.toString()
//                fragmentPointBinding.recyclerViewPostListResult.adapter?.notifyDataSetChanged()
            }
        }


        userCouponViewModel.run {
            userCouponDataList.observe(userActivity){
                val couponCount = userCouponDataList.value?.filter { c-> c.used.toBoolean() }?.size
                fragmentMyPageBinding.myPageCouponCount.text = couponCount.toString()
            }
        }

        reviewViewModel.run {
            reviewDataList.observe(userActivity){
                val reviewCount = reviewDataList.value?.size
                fragmentMyPageBinding.myPageReviewCount.text = reviewCount.toString()
            }
        }
        subscribeViewModel.run {
            followerList.observe(userActivity){
                val followerCount = followerList.value?.size
                fragmentMyPageBinding.myPageFollowerCount.text = followerCount.toString()
            }
        }

        orderViewModel.run {
            orderDataList.observe(userActivity){
                val orderPackingCount = orderDataList.value?.filter { it.status=="PACKING" }?.size
                val orderShippingCount = orderDataList.value?.filter { it.status=="SHIPPING" }?.size
                val orderArrivingCount = orderDataList.value?.filter { it.status=="ARRIVING" }?.size
                val orderSuccessCount = orderDataList.value?.filter { it.status=="SUCCESS" }?.size

                fragmentMyPageBinding.run {
                    myPageDeliverStatusPackingCount.text = orderPackingCount.toString()
                    myPageDeliverStatusShippingCount.text = orderShippingCount.toString()
                    myPageDeliverStatusArrivingCount.text = orderArrivingCount.toString()
                    myPageDeliverStatusSuccessCount.text = orderSuccessCount.toString()
                }

            }

            productDataList.observe(userActivity){
                val productBasic = productDataList.value?.map { p -> p.category.get(0)}
                val productPrepareCount = productBasic?.filter { it.toString()=="1"}?.size
                val productManagingCount = productBasic?.filter { it.toString()=="2"}?.size
                val productHobbyCount = productBasic?.filter { it.toString()=="3"}?.size
                val productDatingCount = productBasic?.filter { it.toString()=="4"}?.size

                fragmentMyPageBinding.run {
                    myPageBasicItemStatusPrepareCount.text = productPrepareCount.toString()
                    myPageBasicItemStatusManagingCount.text = productManagingCount.toString()
                    myPageBasicItemStatusHobbyCount.text = productHobbyCount.toString()
                    myPageBasicItemStatusDatingCount.text = productDatingCount.toString()
                }

            }
        }



        fragmentMyPageBinding.run {
            myPageToolbar.run {
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_item_search -> {
                            val intent = Intent(userActivity, SearchActivity::class.java)
                            startActivity(intent)
                        }
                        R.id.menu_item_cart -> {
                            userActivity.replaceFragment(UserActivity.CART_FRAGMENT, true, null)
                        }
                    }
                    true
                }
            }

            myPageUserImg.run {
                userActivity.getUserProfileImg(userTemp,this)
            }

            myPageBtnToEditUser.setOnClickListener {
                userActivity.replaceFragment(UserActivity.EDIT_USER_FRAGMENT,true,null)
            }

            // todo : 로그아웃
//            myPageBtnToSignOut.setOnClickListener{
//                if(auth == null){
//                    Log.d("teataaa", "not")
//                } else{
//                    Log.d("teataaa", "${auth.currentUser?.email}")
//                    auth.signOut()
//                }
//                val intent = Intent(userActivity, AuthActivity::class.java)
//                startActivity(intent)
//                userActivity.finish()
//            }

            myPageUserNick.text = userTemp.nickname

            myPageToPoint.run {
//                Log.d("포인트",pointSum.toString())
//                myPagePointCount.text = pointSum.toString()
                pointViewModel.getPointListByUser(userTemp.idx)

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

            myPageToUserPage.run {
                reviewViewModel.getReviewListByUser(userTemp.idx)
                subscribeViewModel.getFollowerAll(userTemp.idx)
                setOnClickListener {
                    userActivity.replaceFragment(UserActivity.USER_PAGE_FRAGMENT, true, null)
                }
            }

            // 아이템 추천 받으러 가기
            myPageBtnToRecommend.setOnClickListener {
                val intent = Intent(userActivity, RecommendActivity::class.java)
                startActivity(intent)
            }

            orderViewModel.getOrderListByUser(userTemp.idx)

            // 배송 현황
            myPageDeliverStatusPacking.run {
                setOnClickListener{
                    val intent = Intent(userActivity, ParcelActivity::class.java)
                    startActivity(intent)
                }
            }
            myPageDeliverStatusShipping.run {
                setOnClickListener{
                    val intent = Intent(userActivity, ParcelActivity::class.java)
                    startActivity(intent)

                }
            }
            myPageDeliverStatusArriving.run {
                setOnClickListener{
                    val intent = Intent(userActivity, ParcelActivity::class.java)
                    startActivity(intent)

                }
            }
            myPageDeliverStatusSuccess.run {
                setOnClickListener{
                    val intent = Intent(userActivity, ParcelActivity::class.java)
                    startActivity(intent)
                }
            }


            // 기본 템 보유 현황
            myPageBasicItemStatusHobby.run {
                setOnClickListener{
                    userActivity.replaceFragment(UserActivity.PURCHASE_FRAGMENT,true,null)
                }
            }

            myPageBasicItemStatusDating.run {
                setOnClickListener{
                    userActivity.replaceFragment(UserActivity.PURCHASE_FRAGMENT,true,null)
                }
            }

            myPageBasicItemStatusPrepare.run {
                setOnClickListener{
                    userActivity.replaceFragment(UserActivity.PURCHASE_FRAGMENT,true,null)
                }
            }

            myPageBasicItemStatusManaging.run {
                setOnClickListener{
                    userActivity.replaceFragment(UserActivity.PURCHASE_FRAGMENT,true,null)
                }
            }

            // 내 구독리스트 확인하러 가기
            myPageBtnToSubscribe.setOnClickListener {
                val intent = Intent(userActivity, SubscribeActivity::class.java)
                startActivity(intent)
            }

            userBottomNavigationView.run {
//                selectedItemId = R.id.userFragment
//                setOnItemSelectedListener {
//                    when (it.itemId) {
//                        R.id.userFragment -> {
//
//
//                        }
//                        R.id.rankFragment ->{
//
//
//                        }
//                        R.id.categoryFragment->{
//                            val intent = Intent(userActivity, CategoryActivity::class.java)
//                            startActivity(intent)
//
//                        }
//                        R.id.recommendFragment ->{
//
//                        }
//                    }
//                    return@setOnItemSelectedListener true
//                }
            }


        }

        return fragmentMyPageBinding.root
    }






}