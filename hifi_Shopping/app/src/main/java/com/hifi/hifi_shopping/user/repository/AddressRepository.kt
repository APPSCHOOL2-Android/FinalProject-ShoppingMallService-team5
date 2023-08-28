package com.hifi.hifi_shopping.user.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.hifi.hifi_shopping.user.model.AddressDataClass
import com.hifi.hifi_shopping.user.model.UserDataClass

class AddressRepository {
    companion object{
        fun getAddressAllByUserIdx(userIdx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val addressDataRef = database.getReference("AddressData")

            addressDataRef.orderByChild("userIdx").equalTo(userIdx).get()
                .addOnCompleteListener(callback1)

        }

        fun modifyAddressInfo(addressDataClass: AddressDataClass, callback1: (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val addressDataRef = database.getReference("AddressData")

            addressDataRef.orderByChild("idx").equalTo(addressDataClass.idx).get().addOnCompleteListener {
                for(a1 in it.result.children){
                    a1.ref.child("address").setValue(addressDataClass.address).addOnCompleteListener(callback1)
                }
            }
        }
    }
}