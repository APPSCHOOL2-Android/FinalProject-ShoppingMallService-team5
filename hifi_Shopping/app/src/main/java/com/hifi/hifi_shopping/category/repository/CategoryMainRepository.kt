package com.hifi.hifi_shopping.category.repository

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.hifi.hifi_shopping.category.model.CategoryMainProduct
import com.hifi.hifi_shopping.category.model.CategoryMainReview
import com.hifi.hifi_shopping.category.model.CategoryMainSubscribe
import com.hifi.hifi_shopping.category.model.CategoryMainUser

class CategoryMainRepository {

    lateinit var allProductList: List<CategoryMainProduct>

    val storage = FirebaseStorage.getInstance()

    var productMap = mutableMapOf<String, CategoryMainProduct>()

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

            allProductList = allProductList.sortedBy { product ->
                var price = "" + product.price
                price = price.replace("원", "")
                price = price.replace(",", "")
                price
            }

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

    fun getProductWithWorthJustInfo(productWorth: Int, productCount: Int, callback: (List<CategoryMainProduct>) -> Unit) {
        val range = if (productWorth + 6 > productCount) {
            productCount
        } else {
            productWorth + 6
        }

        val resultList = allProductList.slice(productWorth until range)
        callback(resultList.reversed())
    }

    val databaseImg = FirebaseDatabase.getInstance().getReference("ProductImgData")

    fun getProductImgUrl(idx: Int, callback: (String) -> Unit) {
//        val database = FirebaseDatabase.getInstance().getReference("ProductImgData")

        databaseImg.orderByChild("productIdx").equalTo(allProductList[idx].idx).get().addOnCompleteListener {
            it.result.children.forEach {
                if ("true" == it.child("default").value as String && "1" == it.child("omgOrder").value as String) {
                    val filename = it.child("imgSrc").value as String
                    val fileRef = storage.reference.child("product/$filename")
                    fileRef.downloadUrl.addOnCompleteListener {
                        allProductList[idx].imgSrc = it.result.toString()
                        callback(it.result.toString())
                    }
                    return@forEach
                }
            }
        }
    }

    fun getReview(categoryNum: String, callback: (List<CategoryMainReview>) -> Unit) {
        val database = FirebaseDatabase.getInstance()

        val reviewRef = database.getReference("ReviewData")

        val productRef = database.getReference("ProductData")

        var completeFlag = 0

        var reviewList: List<CategoryMainReview> = emptyList()

        val tProductMap: MutableMap<String, CategoryMainProduct> = mutableMapOf()

        reviewRef.get().addOnCompleteListener {
            reviewList = it.result.children.map {
                CategoryMainReview(
                    it.child("idx").value as String,
                    it.child("productIdx").value as String,
                    it.child("title").value as String,
                    it.child("context").value as String,
                    it.child("score").value as String,
                    it.child("writerIdx").value as String,
                    it.child("likeCnt").value as String,
                    it.child("date").value as String,
                    it.child("imgSrc").value as String
                )
            }

            completeFlag += 1

            if (completeFlag == 2) {
                getReviewWithCategory(categoryNum, callback, reviewList)
            }
        }

        productRef.get().addOnCompleteListener {
            it.result.children.forEach { product ->
                val productIdx = product.child("idx").value as String
                tProductMap[productIdx] = CategoryMainProduct(
                    productIdx,
                    product.child("name").value as String,
                    product.child("price").value as String,
                    product.child("category").value as String,
                    ""
                )
            }
            productMap = tProductMap

            completeFlag += 1

            if (completeFlag == 2) {
                getReviewWithCategory(categoryNum, callback, reviewList)
            }
        }
    }

    fun getReviewWithCategory(
        categoryNum: String,
        callback: (List<CategoryMainReview>) -> Unit,
        reviewList: List<CategoryMainReview>
    ) {
        val reviewListInCategory = mutableListOf<CategoryMainReview>()

        reviewList.forEach { review ->
            val reviewCategoryNum = productMap[review.productIdx]?.categoryNum ?: ""
            if (categoryNum <= reviewCategoryNum && reviewCategoryNum <= categoryNum + "\uf8ff") {
                reviewListInCategory.add(review)
            }
        }

        val resultList = reviewListInCategory.sortedByDescending { it.likeCnt.toInt() }

        callback(resultList)
    }

    fun getUser(userIdx: String, callback: (CategoryMainUser) -> Unit) {
        val userRef = FirebaseDatabase.getInstance().getReference("UserData")

        userRef.orderByChild("idx").equalTo(userIdx).get().addOnCompleteListener {
            it.result.children.forEach {
                val categoryMainUser = CategoryMainUser(
                    it.child("idx").value as String,
                    it.child("nickname").value as String,
                    ""
                )

                val filename = "user/" + it.child("profileImg").value as String
                val fileRef = storage.reference.child(filename)
                fileRef.downloadUrl.addOnCompleteListener {
                    categoryMainUser.profileImg = it.result.toString()
                    callback(categoryMainUser)
                }
            }
        }
    }

    fun getUserFollowerCnt(userIdx: String, currentUserIdx: String, callback: (Int, Boolean) -> Unit) {
        val subscribeRef = FirebaseDatabase.getInstance().getReference("SubscribeData")

        subscribeRef.orderByChild("userIdx").equalTo(userIdx).get().addOnCompleteListener {
            var subscribing = false

            it.result.children.forEach {
                if (it.child("followerIdx").value as String == currentUserIdx) {
                    subscribing = true
                    return@forEach
                }
            }

            callback(it.result.childrenCount.toInt(), subscribing)
        }
    }

    fun getUserItemCnt(userIdx: String, callback: (Int) -> Unit) {
        // 주문 정보 조회하는 걸로 교체
        val orderRef = FirebaseDatabase.getInstance().getReference("OrderData")

        orderRef.orderByChild("buyerIdx").equalTo(userIdx).get().addOnCompleteListener {
            val productSet = mutableSetOf<String>()

            it.result.children.forEach {
                productSet.add(it.child("productIdx").value as String)
            }
            callback(productSet.size)
        }
    }

    fun getUserReviewCnt(userIdx: String, callback: (Int) -> Unit) {
        val reviewRef = FirebaseDatabase.getInstance().getReference("ReviewData")

        reviewRef.orderByChild("writerIdx").equalTo(userIdx).get().addOnCompleteListener {
            callback(it.result.children.toList().size)
        }
    }

    fun getReviewProductInfo(productIdx: String, callback: (CategoryMainProduct) -> Unit) {
        val product = productMap[productIdx]
        if (product?.imgSrc?.isNotEmpty() == true) {
            callback(product)
        } else {
            databaseImg.orderByChild("productIdx").equalTo(productIdx).get().addOnCompleteListener {
                it.result.children.forEach {
                    if ("true" == it.child("default").value as String && "1" == it.child("omgOrder").value as String) {
                        val filename = "product/" + it.child("imgSrc").value as String
                        val fileRef = storage.reference.child(filename)
                        fileRef.downloadUrl.addOnCompleteListener {
                            productMap[productIdx]?.imgSrc = it.result.toString()
                            callback(productMap[productIdx] ?: CategoryMainProduct("", "", "", "", ""))
                        }
                        return@forEach
                    }
                }
            }
        }
    }

    fun getProductRatingInfo(productIdx: String, callback: (Double, Long) -> Unit) {
        val reviewRef = FirebaseDatabase.getInstance().getReference("ReviewData")

        reviewRef.orderByChild("productIdx").equalTo(productIdx).get().addOnCompleteListener {
            val reviewCnt = it.result.childrenCount

            var totalScore = 0.0

            it.result.children.forEach {
                totalScore += (it.child("score").value as String).toDouble()
            }

            callback(totalScore / reviewCnt / 2, reviewCnt)
        }
    }

    fun setSubscribe(userIdx: String, followerIdx: String, subscribing: Boolean, callbackViewModel: () -> Unit, callback: () -> Unit) {
        val subscribeRef = FirebaseDatabase.getInstance().getReference("SubscribeData")

        if (subscribing) {
            val categoryMainSubscribe = CategoryMainSubscribe(userIdx, followerIdx)

            subscribeRef.push().setValue(categoryMainSubscribe).addOnCompleteListener {
                callback()
                callbackViewModel()
            }
        } else {
            subscribeRef.orderByChild("userIdx").equalTo(userIdx).get().addOnCompleteListener {
                it.result.children.forEach {
                    if (it.child("followerIdx").value as String == followerIdx) {
                        it.ref.removeValue().addOnCompleteListener {
                            callback()
                            callbackViewModel()
                        }
                    }
                }
            }
        }
    }
}