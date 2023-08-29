package com.hifi.hifi_shopping.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoryViewModel: ViewModel() {

    var currentUserIdx = ""

    var searchSubCategory = MutableLiveData<Boolean>(false)

    fun setSearchSubCategory(setValue: Boolean) {
        searchSubCategory.value = setValue
    }

    var navControllerDestination = MutableLiveData<Int>()

    fun setNavControllerDestination(setValue: Int) {
        navControllerDestination.value = setValue
    }

    var showProductOrReview = MutableLiveData<ContentType>()

    fun setShowProductOrReview(setValue: ContentType) {
        showProductOrReview.value = setValue
    }
}

enum class ContentType {
    PRODUCT,
    REVIEW
}