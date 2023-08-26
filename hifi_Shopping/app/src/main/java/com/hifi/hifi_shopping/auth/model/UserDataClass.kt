package com.hifi.hifi_shopping.auth.model

data class UserDataClass(
    var idx: String, // 유저 인덱스
    val email: String, // 유저 아이디(email)
    val pw: String, // 유저 비밀번호
    val nickname: String, // 유저 닉네임
    val verify: String, // 유저 본인 인증 여부
    val phoneNum: String, // 유저 전화번호
    val profileImg: String // 유저 프로필 이미지
)