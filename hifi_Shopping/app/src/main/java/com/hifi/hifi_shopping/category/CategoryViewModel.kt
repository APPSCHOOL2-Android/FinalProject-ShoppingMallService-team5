package com.hifi.hifi_shopping.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoryViewModel: ViewModel() {

    var searchSubCategory = MutableLiveData<Boolean>()

    fun setSearchSubCategory(setValue: Boolean) {
        searchSubCategory.value = setValue
    }
}