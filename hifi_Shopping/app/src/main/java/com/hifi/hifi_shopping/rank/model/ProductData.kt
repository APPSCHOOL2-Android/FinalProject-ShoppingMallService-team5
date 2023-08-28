package com.example.mini01_lbs01.model

// 임시
data class ProductDataClass (
    val Idx: String, // 제품 인덱스 (ProductImgData에서 여기로 접근)
    val name: String, // 제품 명
    val price: String, // 제품 가격
    val context: String, // 제품 설명 내용
    val categoryNum: String, // 카테고리 고유번호
    val pointAmount: String, // 포인트양
    val sellerIdx: String // 판매자 인덱스
)