package com.adzu.liecapp.api.main.models

import com.google.gson.annotations.SerializedName

data class VehicleInfoResponse(
    @SerializedName("data")
    val vehicleInfo: VehicleInfo,
    val message: String
)