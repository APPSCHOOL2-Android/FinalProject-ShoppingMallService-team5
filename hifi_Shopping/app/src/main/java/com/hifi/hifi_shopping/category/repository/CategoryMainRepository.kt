package com.hifi.hifi_shopping.category.repository

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.category.model.CategoryMainProduct

class CategoryMainRepository {

    lateinit var allProductList: List<CategoryMainProduct>

    fun getProduct(categoryNum: String, callback: (List<CategoryMainProduct>) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("ProductData")

        database.orderByChild("category").startAt(categoryNum).endAt(categoryNum + "\uf8ff").get().addOnCompleteListener {
            allProductList = it.result.children.map { product ->
                CategoryMainProduct(
                    product.child("idx").value as String,
                    product.child("name").value as String,
                    product.child("price").value as String,
                    product.child("category").value as String,
                    ""
                )
            }

            allProductList = allProductList.sortedBy { it.price.toInt() }

            callback(allProductList)
        }
    }

    fun getProductWithWorth(productWorth: Int, productCount: Int, callback: (List<CategoryMainProduct>) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("ProductImgData")

        val range = if (productWorth + 6 > productCount) {
            productCount
        } else {
            productWorth + 6
        }

        var fetchCount = range - productWorth

        for (i in productWorth until range) {
            val product = allProductList[i]

            if (product.imgSrc.isNotEmpty()) {
                fetchCount -= 1
                if (fetchCount == 0) {
                    val resultList = allProductList.slice(productWorth until range)
                    callback(resultList.reversed())
                }
                continue
            }

            database.orderByChild("productIdx").equalTo(product.idx).get().addOnCompleteListener {
                it.result.children.forEach {
                    if ("true" == it.child("default").value as String && "1" == it.child("omgOrder").value as String) {
                        allProductList[i].imgSrc = it.child("imgSrc").value as String
                    }
                }
                fetchCount -= 1
                if (fetchCount == 0) {
                    val resultList = allProductList.slice(productWorth until range)
                    callback(resultList.reversed())
                }
            }

//            database.orderByChild("productIdx").equalTo(product.idx).get().addOnCompleteListener {
//                it.result.children.forEach {
//                    if ("true" == it.child("default").value as String && "1" == it.child("omgOrder").value as String) {
//                        val filename = it.child("imgSrc").value as String
//                        val storage = FirebaseStorage.getInstance()
//                        val fileRef = storage.reference.child(filename)
//                        fileRef.downloadUrl.addOnCompleteListener {
//                            allProductList[i].imgSrc = it.result.toString()
//
//                            fetchCount -= 1
//                            if (fetchCount == 0) {
//                                val resultList = allProductList.slice(productWorth until range)
//                                callback(resultList.reversed())
//                            }
//                        }
//                    }
//                }
//            }
        }
    }
}