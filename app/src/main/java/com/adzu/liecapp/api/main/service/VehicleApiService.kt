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


interface VehicleApiService {
    @GET("vehicles/{plateNumber}")
    suspend fun getVehicleInfo(@Path("plateNumber") plateNumber: String): Response<VehicleInfoResponse>

    @GET("vehicles")
    suspend fun getAllVehicles(): Response<List<VehicleInfoResponse>>

    @POST("vehicles/entry-record")
    @FormUrlEncoded
    suspend fun insertEntryRecord(@Field("plateNumber") plateNumber: String): Response<Unit>
}