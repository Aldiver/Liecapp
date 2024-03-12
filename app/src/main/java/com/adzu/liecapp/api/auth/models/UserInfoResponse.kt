package com.adzu.liecapp.api.auth.models

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("user_info")
    val userInfo: UserInfo,
    val message: String
)