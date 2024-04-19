package com.adzu.liecapp.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.adzu.liecapp.api.auth.models.Auth
import com.adzu.liecapp.api.auth.models.LoginResponse
import com.adzu.liecapp.api.auth.models.UserRegister
import com.adzu.liecapp.api.auth.repository.AuthRepository
import com.adzu.liecapp.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): BaseViewModel() {

    private val _loginResponse = MutableLiveData<ApiResponse<LoginResponse>>()
    val loginResponse = _loginResponse

    private val _registerResponse = MutableLiveData<ApiResponse<LoginResponse>>()
    val registerResponse = _registerResponse

    fun login(auth: Auth, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _loginResponse,
        coroutinesErrorHandler
    ) {
        authRepository.login(auth)
    }

    fun register(user: UserRegister, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _registerResponse,
        coroutinesErrorHandler
    ) {
        authRepository.register(user)
    }
}