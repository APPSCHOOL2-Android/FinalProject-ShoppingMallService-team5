package com.hifi.hifi_shopping.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hifi.hifi_shopping.search.model.SearchData
import com.hifi.hifi_shopping.search.model.SearchProduct

class SearchViewModel: ViewModel() {

    val searchRepository = SearchRepository()

    var recentSearchWordList = MutableLiveData<List<String>>()

    var searchResultList = MutableLiveData<List<SearchProduct>>()

    fun getRecentSearchWord(userIdx: String) {
        searchRepository.getRecentSearchWord(userIdx) {
            recentSearchWordList.value = it
        }
    }

    fun addSearchWord(searchData: SearchData) {
        searchRepository.addSearchWord(searchData)
    }

    fun getSearchResult(searchWord: String) {
        searchRepository.getSearchResult(searchWord) {
            searchResultList.value = it
        }
    }

    fun getProductImg(productIdx: String, callback: (String) -> Unit) {
        searchRepository.getProductImg(productIdx, callback)
    }

    fun removeRecentSearchWord(word: String, userIdx: String) {
        searchRepository.removeRecentSearchWord(word, userIdx)
    }
}