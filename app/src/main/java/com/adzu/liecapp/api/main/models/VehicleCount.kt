package com.adzu.liecapp.api.main.models

data class VehicleCount(
    val total_valid_vehicles: Int,
    val total_expired_vehicles: Int,
    val total_guest_vehicles: Int
)