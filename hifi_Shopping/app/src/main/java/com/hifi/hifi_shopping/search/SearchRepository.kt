package com.hifi.hifi_shopping.search

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.category.model.CategoryMainProduct
import com.hifi.hifi_shopping.search.model.SearchData
import com.hifi.hifi_shopping.search.model.SearchProduct

class SearchRepository {

    val searchRef = FirebaseDatabase.getInstance().getReference("SearchData")

    val productRef = FirebaseDatabase.getInstance().getReference("ProductData")

    val productImgRef = FirebaseDatabase.getInstance().getReference("ProductImgData")

    val storage = FirebaseStorage.getInstance()

    fun getRecentSearchWord(userIdx: String, callback: (List<String>) -> Unit) {
        searchRef.orderByChild("userIdx").equalTo(userIdx).ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val searchWordMap = mutableMapOf<String, Long>()

                snapshot.children.forEach {
                    val context = it.child("context").value as String
                    val date = (it.child("date").value as String).toLong()
                    searchWordMap[context] = date
                }

                val searchWordList = searchWordMap.toList()
                val searchWordListSorted = searchWordList.sortedByDescending { it.second }
                callback(
                    searchWordListSorted.map {
                        it.first
                    }
                )
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("brudenell", "데이터베이스 취소")
            }
        })
    }

    fun addSearchWord(searchData: SearchData) {
        searchRef.push().setValue(searchData)
    }

    fun getSearchResult(searchWord: String, callback: (List<SearchProduct>) -> Unit) {
        productRef.get().addOnCompleteListener {
            val searchResultList = mutableListOf<SearchProduct>()

            it.result.children.forEach {
                val productName = it.child("name").value as String
                val productContext = it.child("context").value as String

                if (productName.contains(searchWord) || productContext.contains(searchWord)) {
                    searchResultList.add(
                        SearchProduct(
                            it.child("idx").value as String,
                            productName
                        )
                    )
                }
            }

            callback(searchResultList)
        }
    }

    fun getProductImg(productIdx: String, callback: (String) -> Unit) {
        productImgRef.orderByChild("productIdx").equalTo(productIdx).get().addOnCompleteListener {
            it.result.children.forEach {
                if ("true" == it.child("default").value as String && "1" == it.child("omgOrder").value as String) {
                    val filename = "product/" + it.child("imgSrc").value as String
                    val fileRef = storage.reference.child(filename)
                    fileRef.downloadUrl.addOnCompleteListener {
                        callback(it.result.toString())
                    }
                    return@forEach
                }
            }
        }
    }

    fun removeRecentSearchWord(word: String, userIdx: String) {
        searchRef.orderByChild("userIdx").equalTo(userIdx).get().addOnCompleteListener {
            it.result.children.forEach {
                if (it.child("context").value as String == word) {
                    it.ref.removeValue()
                }
            }
        }
    }
}