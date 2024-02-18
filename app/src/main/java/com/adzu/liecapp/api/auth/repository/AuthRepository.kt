package com.adzu.liecapp.api.auth.repository

import com.adzu.liecapp.api.auth.models.Auth
import com.adzu.liecapp.api.auth.service.AuthApiService
import com.adzu.liecapp.utils.apiRequestFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
) {
    fun login(auth: Auth) = apiRequestFlow {
        authApiService.login(auth)
    }
}