package com.adzu.liecapp.api.main.service

import com.adzu.liecapp.api.auth.models.UserInfoResponse
import retrofit2.Response
import retrofit2.http.GET


interface MainApiService {
    @GET("user/info")
    suspend fun getUserInfo(): Response<UserInfoResponse>
}