package com.adzu.liecapp.api.auth.models

import com.google.gson.annotations.SerializedName

data class UserRegister(
    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
