package com.hifi.hifi_shopping.buy.buy_repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.buy.datamodel.CartData
import com.hifi.hifi_shopping.buy.datamodel.OrderData

class OrderItemRepository {

    companion object{

        fun setCartData(cartData: CartData, callback1: (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val CartDataRef = database.getReference("CartData")
            CartDataRef.push().setValue(cartData).addOnCompleteListener(callback1)
        }
        fun getCartData(idx: String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val CartDataRef = database.getReference("CartData")
            CartDataRef.orderByChild("userIdx").equalTo(idx).get().addOnCompleteListener(callback1)
        }
        fun getProductFAQData(idx: String, callback1: (Task<DataSnapshot>) -> Unit, callback2: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val FAQDataRef = database.getReference("FAQData")
            FAQDataRef.orderByChild("productIdx").equalTo(idx).get().addOnCompleteListener(callback1).addOnCompleteListener(callback2)
        }

        fun getProductNormalReview(idx: String, callback1: (Task<DataSnapshot>) -> Unit, callback2: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val reviewDataRef = database.getReference("ReviewData")
            reviewDataRef.orderByChild("productIdx").equalTo(idx).get().addOnCompleteListener(callback1)
                .addOnCompleteListener(callback2)
        }
        fun setOrderData(orderData: OrderData){
            val database = FirebaseDatabase.getInstance()
            val orderDataRef = database.getReference("OrderData")
            orderDataRef.push().setValue(orderData)
        }

        fun getOrderProductData(idx: String, callback1: (Task<DataSnapshot>)-> Unit, callback2: (Task<DataSnapshot>)-> Unit){
            val database = FirebaseDatabase.getInstance()
            val productDataRef = database.getReference("ProductData")
            productDataRef.orderByChild("idx").equalTo(idx).get().addOnCompleteListener (callback1)
                .addOnCompleteListener(callback2)
        }

        fun getProductImgSrc(idx:String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val productDataRef = database.getReference("ProductImgData")
            productDataRef.orderByChild("productIdx").equalTo(idx).get()
                .addOnCompleteListener(callback1)
        }
        fun getProductImg(src: String, callback1: (Task<Uri>) -> Unit){
            val storage = FirebaseStorage.getInstance()
            val fileRef = storage.getReference("product").child(src)
            // 데이터를 가져올 수 있는 경로를 가져온다.
            fileRef.downloadUrl.addOnCompleteListener(callback1)
        }
    }
}