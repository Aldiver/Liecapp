package com.adzu.liecapp.api.auth.service

import com.adzu.liecapp.api.auth.models.Auth
import com.adzu.liecapp.api.auth.models.LoginResponse
import com.adzu.liecapp.api.auth.models.UserRegister
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(
        @Body auth: Auth,
    ): Response<LoginResponse>

    @GET("auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String,
    ): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(
        @Body user: UserRegister,
    ): Response<LoginResponse>
}