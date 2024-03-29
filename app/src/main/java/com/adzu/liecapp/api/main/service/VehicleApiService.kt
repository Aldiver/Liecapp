package com.adzu.liecapp.api.main.service

import com.adzu.liecapp.api.main.models.VehicleCount
import com.adzu.liecapp.api.main.models.VehicleInfo
import com.adzu.liecapp.api.main.models.VehicleInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface VehicleApiService {
    @GET("vehicles/{plateNumber}")
    suspend fun getVehicleInfo(@Path("plateNumber") plateNumber: String): Response<VehicleInfoResponse>

    @GET("vehicles")
    suspend fun getAllVehicles(): Response<List<VehicleInfo>>

    @GET("summary/vehicles")
    suspend fun getTotalVehicles(): Response<VehicleCount>

    @POST("vehicles/entry-record")
    suspend fun insertVehicleAndEntryRecord(@Body vehicleInfo: VehicleInfo): Response<Unit>

    @POST("vehicles/entry-record-valid")
    @FormUrlEncoded
    suspend fun insertEntryRecord(@Field("plateNumber") plateNumber: String): Response<Unit>
}