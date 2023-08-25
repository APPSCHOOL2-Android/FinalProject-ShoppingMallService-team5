package com.hifi.hifi_shopping.buy.buy_repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.buy.buy_vm.AddressData
import com.hifi.hifi_shopping.buy.buy_vm.OrderUserCoupon


class OrderUserRepository {
    companion object{

        fun getOrderUserPossibleCoupon(idx: String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userCoupontData = database.getReference("CouponData")
            userCoupontData.orderByChild("idx").equalTo(idx).get().addOnCompleteListener(callback1)
        }
        fun getOrderUserCoupon(userIdx: String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userCoupontData = database.getReference("UserCouponData")
            userCoupontData.orderByChild("userIdx").equalTo(userIdx).get().addOnCompleteListener(callback1)
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