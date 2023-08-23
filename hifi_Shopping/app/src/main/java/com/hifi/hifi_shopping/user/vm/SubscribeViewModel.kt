package com.hifi.hifi_shopping.user.vm

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

    var followerList = MutableLiveData<MutableList<UserDataClass>>()
    var followingList = MutableLiveData<MutableList<UserDataClass>>()

    fun getFollowerAll(useridx:String){

        val tempList = mutableListOf<SubscribeDataClass>()
        val tempList2 = mutableListOf<UserDataClass>()

        SubscribeRepository.getSubscribeListByUserIdx(useridx){
            for(c1 in it.result.children){
                val userIdx = c1.child("userIdx").value as String
                val followerIdx = c1.child("followerIdx").value as String

                val s1= SubscribeDataClass(userIdx,followerIdx)
                tempList.add(s1)

                UserRepository.getUserInfoByUserIdx(userIdx){
                    for(c2 in it.result.children){
                        val idx = c1.child("userIdx").value as String
                        val email = c1.child("email").value as String
                        val pw = c1.child("pw").value as String
                        val nickname = c1.child("nickname").value as String
                        val phoneNum = c1.child("phoneNum").value as String
                        val profileImg = c1.child("profileImg").value as String
                        val verify = c1.child("verify").value as String

                        val u = UserDataClass(idx, email, pw, nickname, verify, phoneNum, profileImg)

                        tempList2.add(u)
                    }

                }
            }
            followerList.value = tempList2
        }
    }

    fun getFollowingAll(followeridx:String){

        val tempList = mutableListOf<SubscribeDataClass>()
        val tempList2 = mutableListOf<UserDataClass>()

        SubscribeRepository.getSubscribeListByFollowerIdx(followeridx){
            for(c1 in it.result.children){
                val userIdx = c1.child("userIdx").value as String
                val followerIdx = c1.child("followerIdx").value as String

                val s1= SubscribeDataClass(userIdx,followerIdx)
                tempList.add(s1)

                UserRepository.getUserInfoByUserIdx(userIdx){
                    for(c2 in it.result.children){
                        val idx = c1.child("userIdx").value as String
                        val email = c1.child("email").value as String
                        val pw = c1.child("pw").value as String
                        val nickname = c1.child("nickname").value as String
                        val phoneNum = c1.child("phoneNum").value as String
                        val profileImg = c1.child("profileImg").value as String
                        val verify = c1.child("verify").value as String

                        val u = UserDataClass(idx, email, pw, nickname, verify, phoneNum, profileImg)

                        tempList2.add(u)
                    }

                }
            }
            followingList.value = tempList2
        }
    }


}