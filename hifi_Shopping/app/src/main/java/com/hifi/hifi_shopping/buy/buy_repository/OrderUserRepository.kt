package com.hifi.hifi_shopping.buy.buy_repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.buy.datamodel.AddressData


class OrderUserRepository {
    companion object{

        fun getOrderUserSubscribeUserImg(imgSrc: String, callback1: (Task<Uri>) -> Unit){
            val storage = FirebaseStorage.getInstance()
            val fileRef = storage.getReference("user").child(imgSrc)
            // 데이터를 가져올 수 있는 경로를 가져온다.
            fileRef.downloadUrl.addOnCompleteListener(callback1)
        }

        fun getOrderUserSubscribeUserReview(subUserIdx: String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val reviewDataRef = database.getReference("ReviewData")
            reviewDataRef.orderByChild("writerIdx").equalTo(subUserIdx).get().addOnCompleteListener(callback1)
        }
        fun getOrderUserSubscribeUser(idx: String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val subscribeDataRef = database.getReference("SubscribeData")
            subscribeDataRef.orderByChild("userIdx").equalTo(idx).get().addOnCompleteListener(callback1)
        }

        fun getOrderUser(idx: String, callback1: (Task<DataSnapshot>) -> Unit, callback2: (Task<DataSnapshot>) -> Unit, callback3: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")
            userDataRef.orderByChild("idx").equalTo(idx).get().addOnCompleteListener(callback1).addOnCompleteListener(callback2)
                .addOnCompleteListener(callback3)
        }
        fun getOrderUserPossibleCoupon(idx: String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val couponDataRef = database.getReference("CouponData")
            couponDataRef.orderByChild("idx").equalTo(idx).get().addOnCompleteListener(callback1)
        }
        fun getOrderUserCoupon(userIdx: String, callback1: (Task<DataSnapshot>) -> Unit, callback2: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userCoupontDataRef = database.getReference("UserCouponData")
            userCoupontDataRef.orderByChild("userIdx").equalTo(userIdx).get().addOnCompleteListener(callback1)
                .addOnCompleteListener(callback2)
        }

        fun setOrderUserCoupon(idx: String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userCoupontDataRef = database.getReference("UserCouponData")
            userCoupontDataRef.orderByChild("couponIdx").equalTo(idx).get().addOnCompleteListener{
                for(a1 in it.result.children){
                    a1.ref.child("used").setValue("false")
                }
            }.addOnCompleteListener(callback1)
        }
        fun getOrderUser(idx: String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")
            userDataRef.orderByChild("idx").equalTo(idx).get().addOnCompleteListener(callback1)
        }
        fun setUserAuth(idx: String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("UserData")
            userDataRef.orderByChild("idx").equalTo(idx).get().addOnCompleteListener {
                for(a1 in it.result.children){
                    a1.ref.child("verify").setValue("true")
                }
            }.addOnCompleteListener(callback1)
        }

        fun orderUserGetAddress(userIdx:String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val addressDataRef = database.getReference("AddressData")
            addressDataRef.orderByChild("userIdx").equalTo(userIdx).get().addOnCompleteListener(callback1)
        }

        fun orderUserSetAddress(addressData: AddressData, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val addressDataRef = database.getReference("AddressData")
            addressDataRef.orderByChild("idx").equalTo(addressData.idx).get().addOnCompleteListener {
                for(a1 in it.result.children){
                    a1.ref.child("idx").setValue(addressData.idx)
                    a1.ref.child("userIdx").setValue(addressData.userIdx)
                    a1.ref.child("receiver").setValue(addressData.receiver)
                    a1.ref.child("receiverPhoneNum").setValue(addressData.receiverPhoneNum)
                    a1.ref.child("address").setValue(addressData.address)
                    a1.ref.child("context").setValue(addressData.context)
                }
            }.addOnCompleteListener(callback1)
        }

        fun addOrderUserAddress(addressData: AddressData, callback1: (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val addressDataRef = database.getReference("AddressData")
            addressDataRef.push().setValue(addressData).addOnCompleteListener(callback1)
        }

    }
}