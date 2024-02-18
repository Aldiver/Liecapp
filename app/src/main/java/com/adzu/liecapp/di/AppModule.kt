package com.adzu.liecapp.di

import com.adzu.liecapp.api.auth.repository.AuthRepository
import com.adzu.liecapp.api.auth.service.AuthApiService
import com.adzu.liecapp.api.main.repository.MainRepository
import com.adzu.liecapp.api.main.service.MainApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {

    @Provides
    fun provideAuthRepository(authApiService: AuthApiService) = AuthRepository(authApiService)

    @Provides
    fun provideMainRepository(mainApiService: MainApiService) = MainRepository(mainApiService)
}