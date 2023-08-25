package com.hifi.hifi_shopping.category.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.category.model.CategoryMainProduct
import com.hifi.hifi_shopping.category.repository.CategoryMainRepository

class CategoryMainViewModel: ViewModel() {
    var productList = MutableLiveData<List<CategoryMainProduct>>()

    var allProductList = MutableLiveData<List<CategoryMainProduct>>()

    var productWorth = 0
    var productCount = 0

    val categoryMainRepository = CategoryMainRepository()

    init {
        getProduct()
    }

    fun getProduct() {
        categoryMainRepository.getProduct {
            productCount = it.size
            allProductList.value = it
        }
    }

    fun getProductWithWorth() {
        categoryMainRepository.getProductWithWorth(productWorth, productCount) {
            productList.value = it
        }
    }
}