package com.adzu.liecapp.api.main.models

data class VehicleInfo(
    val _id: String,
    val owner: String,
    val plateNumber: String,
    val validity: String,
    val validityDate: String,
    val type: String,
)