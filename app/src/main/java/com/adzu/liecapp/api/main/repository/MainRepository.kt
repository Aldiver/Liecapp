package com.adzu.liecapp.api.main.repository

import com.adzu.liecapp.api.main.service.MainApiService
import com.adzu.liecapp.utils.apiRequestFlow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val mainApiService: MainApiService,
) {
    fun getUserInfo() = apiRequestFlow {
        mainApiService.getUserInfo()
    }
}