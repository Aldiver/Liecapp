package com.adzu.liecapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.adzu.liecapp.api.auth.service.AuthApiService
import com.adzu.liecapp.api.main.service.EntryRecordApiService
import com.adzu.liecapp.api.main.service.MainApiService
import com.adzu.liecapp.api.main.service.VehicleApiService
import com.adzu.liecapp.utils.AuthAuthenticator
import com.adzu.liecapp.utils.AuthInterceptor
import com.adzu.liecapp.utils.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")
@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager = TokenManager(context)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(tokenManager: TokenManager): AuthAuthenticator =
        AuthAuthenticator(tokenManager)

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl("http://192.168.0.171:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideAuthAPIService(retrofit: Retrofit.Builder): AuthApiService =
        retrofit
            .build()
            .create(AuthApiService::class.java)

    @Singleton
    @Provides
    fun provideMainAPIService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): MainApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(MainApiService::class.java)


        @Singleton
        @Provides
        fun provideVehicleApiService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): VehicleApiService =
            retrofit
                .client(okHttpClient)
                .build()
                .create(VehicleApiService::class.java)

        @Singleton
        @Provides
        fun provideEntryRecordApiService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): EntryRecordApiService =
            retrofit
                .client(okHttpClient)
                .build()
                .create(EntryRecordApiService::class.java)
}