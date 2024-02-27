package com.adzu.liecapp.api.main.models

data class VehicleInfo(
    val _id: String,
    val owner: String,
    val plate_number: String,
    val validity: String,
    val validity_date: String,
    val type: String,
)