package com.hifi.hifi_shopping.category.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.category.model.CategoryMainProduct
import com.hifi.hifi_shopping.category.model.CategoryMainReview
import com.hifi.hifi_shopping.category.model.CategoryMainUser
import com.hifi.hifi_shopping.category.repository.CategoryMainRepository

class CategoryMainViewModel: ViewModel() {
    var productList = MutableLiveData<List<CategoryMainProduct>>()

    var allProductList = MutableLiveData<List<CategoryMainProduct>>()

    var reviewList = MutableLiveData<List<CategoryMainReview>>()

    var productWorth = 0
    var productCount = 0

    var currentUserIdx = ""

    val categoryMainRepository = CategoryMainRepository()

    var updateSubscribeData = MutableLiveData<Boolean>()

    fun getProduct(categoryNum: String) {
        categoryMainRepository.getProduct(categoryNum) {
            productCount = it.size
            allProductList.value = it
        }
    }

    fun getProductWithWorth() {
        categoryMainRepository.getProductWithWorth(productWorth, productCount) {
            productList.value = it
        }
    }

    fun getProductWithWorthJustInfo() {
        categoryMainRepository.getProductWithWorthJustInfo(productWorth, productCount) {
            Log.d("brudenell", "??????")
            productList.value = it
        }
    }

    fun getProductImgUrl(idx: Int, callback: (String) -> Unit) {
        categoryMainRepository.getProductImgUrl(idx, callback)
    }

    fun getReview(categoryNum: String) {
        categoryMainRepository.getReview(categoryNum) {
            reviewList.value = it
        }
    }

    fun getUser(userIdx: String, callback: (CategoryMainUser) -> Unit) {
        categoryMainRepository.getUser(userIdx, callback)
    }

    fun getUserFollowerCnt(userIdx: String, callback: (Int, Boolean) -> Unit) {
        categoryMainRepository.getUserFollowerCnt(userIdx, currentUserIdx, callback)
    }

    fun getUserItemCnt(userIdx: String, callback: (Int) -> Unit) {
        categoryMainRepository.getUserItemCnt(userIdx, callback)
    }

    fun getUserReviewCnt(userIdx: String, callback: (Int) -> Unit) {
        categoryMainRepository.getUserReviewCnt(userIdx, callback)
    }

    fun getReviewProductInfo(productIdx: String, callback: (CategoryMainProduct) -> Unit) {
        categoryMainRepository.getReviewProductInfo(productIdx, callback)
    }

    fun getProductRatingInfo(productIdx: String, callback: (Double, Long) -> Unit) {
        categoryMainRepository.getProductRatingInfo(productIdx, callback)
    }

    fun setSubscribe(userIdx: String, subscribing: Boolean, callback: () -> Unit) {
        val callbackViewModel: () -> Unit = {
            val value = updateSubscribeData.value ?: false
            updateSubscribeData.value = !value
        }

        categoryMainRepository.setSubscribe(userIdx, currentUserIdx, subscribing, callbackViewModel, callback)
    }
}