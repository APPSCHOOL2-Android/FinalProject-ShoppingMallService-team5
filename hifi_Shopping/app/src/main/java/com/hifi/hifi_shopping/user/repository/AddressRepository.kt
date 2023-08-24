package com.hifi.hifi_shopping.user.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class AddressRepository {
    companion object{
        fun getAddressAllByUserIdx(userIdx: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val addressDataRef = database.getReference("AddressData")

            addressDataRef.orderByChild("userIdx").equalTo(userIdx).get()
                .addOnCompleteListener(callback1)

        }
    }
}