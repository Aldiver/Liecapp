package com.adzu.liecapp.api.main.service

import com.adzu.liecapp.api.auth.models.UserInfoResponse
import com.adzu.liecapp.api.main.models.EntryInfoResponse
import com.adzu.liecapp.api.main.models.VehicleInfoResponse
import com.adzu.liecapp.presentation.Screens.Vehicle
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface EntryRecordApiService {
    @GET("entry-records")
    suspend fun getAllEntryRecords(): Response<List<EntryInfoResponse>>
}
