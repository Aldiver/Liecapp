package com.adzu.liecapp.api.main.repository

import com.adzu.liecapp.api.main.service.VehicleApiService
import com.adzu.liecapp.utils.apiRequestFlow
import javax.inject.Inject

class VehicleRepository @Inject constructor(
    private val vehicleApiService: VehicleApiService,
) {
    fun getVehicleInfo(plateNumber: String) = apiRequestFlow {
        vehicleApiService.getVehicleInfo(plateNumber)
    }

    fun getAllVehicles() = apiRequestFlow {
        vehicleApiService.getAllVehicles()
    }

    fun insertEntryRecord(plateNumber: String) = apiRequestFlow {
        vehicleApiService.insertEntryRecord(plateNumber)
    }
}
