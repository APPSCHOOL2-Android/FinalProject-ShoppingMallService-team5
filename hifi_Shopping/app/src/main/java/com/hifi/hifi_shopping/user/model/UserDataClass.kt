package com.hifi.hifi_shopping.user.model

data class UserDataClass(
    var idx: String,
    var email: String,
    var pw: String,
    var nickname: String,
    var verify: Boolean,
    var phoneNum: String,
    var profileImg: String
)