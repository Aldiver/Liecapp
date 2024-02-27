package com.adzu.liecapp.api.main.models

import com.google.gson.annotations.SerializedName

data class EntryInfoResponse(
    @SerializedName("data")
    val vehicleInfo: List<EntryInfo>,
    val message: String
)