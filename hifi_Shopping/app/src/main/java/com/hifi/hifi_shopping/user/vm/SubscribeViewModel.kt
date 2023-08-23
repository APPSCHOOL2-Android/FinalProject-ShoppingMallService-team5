package com.hifi.hifi_shopping.user.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.user.model.CouponDataClass
import com.hifi.hifi_shopping.user.model.SubscribeDataClass
import com.hifi.hifi_shopping.user.model.UserCouponDataClass
import com.hifi.hifi_shopping.user.model.UserDataClass
import com.hifi.hifi_shopping.user.repository.CouponRepository
import com.hifi.hifi_shopping.user.repository.SubscribeRepository
import com.hifi.hifi_shopping.user.repository.UserCouponRepository
import com.hifi.hifi_shopping.user.repository.UserRepository

class SubscribeViewModel : ViewModel() {

    var subscribeList = MutableLiveData<MutableList<SubscribeDataClass>>()
    var followerList = MutableLiveData<MutableList<UserDataClass>>()
    var followingList = MutableLiveData<MutableList<UserDataClass>>()

    fun getFollowerAll(useridx: String) {

        val tempList = mutableListOf<SubscribeDataClass>()
        val tempList2 = mutableListOf<UserDataClass>()

        SubscribeRepository.getSubscribeListByUserIdx(useridx) {
            for (c1 in it.result.children) {
                val userIdx1 = c1.child("userIdx").value as String
                val followerIdx = c1.child("followerIdx").value as String

                val s1 = SubscribeDataClass(userIdx1, followerIdx)
                Log.d("구독", s1.toString())
                tempList.add(s1)
                Log.d("구독2", followerIdx)

            }
            subscribeList.value = tempList

            subscribeList.value?.forEach {
                UserRepository.getUserInfoByUserIdx(it.followerIdx) {
                    for (c2 in it.result.children) {
                        val idx = c2.child("idx").value as String
                        val email = c2.child("email").value as String
                        val pw = c2.child("pw").value as String
                        val nickname = c2.child("nickname").value as String
                        val phoneNum = c2.child("phoneNum").value as String
                        val profileImg = c2.child("profileImg").value as String
                        val verify = c2.child("verify").value as String

                        val u = UserDataClass(idx, email, pw, nickname, verify, phoneNum, profileImg)
                        tempList2.add(u)
                        Log.d("구독3", tempList2.toString())
                    }
                    followerList.value = tempList2
                    Log.d("구독4", followerList.value.toString())
                }

            }
        }

    }

    fun getFollowingAll(useridx: String) {

        val tempList = mutableListOf<SubscribeDataClass>()
        val tempList2 = mutableListOf<UserDataClass>()

        SubscribeRepository.getSubscribeListByUserIdx(useridx) {
            for (c1 in it.result.children) {
                val userIdx1 = c1.child("userIdx").value as String
                val followerIdx = c1.child("followerIdx").value as String

                val s1 = SubscribeDataClass(userIdx1, followerIdx)
                Log.d("내가", s1.toString())
                tempList.add(s1)
                Log.d("내가2", followerIdx)

            }
            subscribeList.value = tempList

            subscribeList.value?.forEach {
                UserRepository.getUserInfoByUserIdx(it.userIdx) {
                    for (c2 in it.result.children) {
                        val idx = c2.child("idx").value as String
                        val email = c2.child("email").value as String
                        val pw = c2.child("pw").value as String
                        val nickname = c2.child("nickname").value as String
                        val phoneNum = c2.child("phoneNum").value as String
                        val profileImg = c2.child("profileImg").value as String
                        val verify = c2.child("verify").value as String

                        val u = UserDataClass(idx, email, pw, nickname, verify, phoneNum, profileImg)
                        tempList2.add(u)
                        Log.d("내가3", tempList2.toString())
                    }
                    followingList.value = tempList2
                    Log.d("내가4", followerList.value.toString())
                }

            }
        }

    }


}