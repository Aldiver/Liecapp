package com.adzu.liecapp.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.adzu.liecapp.api.auth.models.UserInfoResponse
import com.adzu.liecapp.api.main.repository.MainRepository
import com.adzu.liecapp.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
): BaseViewModel() {

    private val _userInfoResponse = MutableLiveData<ApiResponse<UserInfoResponse>>()
    val userInfoResponse = _userInfoResponse

    fun getUserInfo(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _userInfoResponse,
        coroutinesErrorHandler,
    ) {
        mainRepository.getUserInfo()
    }
}