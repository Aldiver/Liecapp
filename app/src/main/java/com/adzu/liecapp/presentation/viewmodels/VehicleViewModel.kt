package com.adzu.liecapp.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.adzu.liecapp.api.main.models.VehicleCount
import com.adzu.liecapp.api.main.models.VehicleInfo
import com.adzu.liecapp.api.main.models.VehicleInfoResponse
import com.adzu.liecapp.api.main.repository.VehicleRepository
import com.adzu.liecapp.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
) : BaseViewModel() {

    private val _vehicleInfoResponse = MutableLiveData<ApiResponse<VehicleInfoResponse>>()
    val vehicleInfoResponse = _vehicleInfoResponse

    private val _allVehiclesResponse = MutableLiveData<ApiResponse<List<VehicleInfo>>>()
    val allVehiclesResponse = _allVehiclesResponse

    private val _insertEntryRecordResponse = MutableLiveData<ApiResponse<Unit>>()
    val insertEntryRecordResponse = _insertEntryRecordResponse

    private val _totalVehicles = MutableLiveData<ApiResponse<VehicleCount>>()
    val totalVehiclesResponse = _totalVehicles

    fun getVehicleInfo(plateNumber: String, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _vehicleInfoResponse,
        coroutinesErrorHandler
    ) {
        vehicleRepository.getVehicleInfo(plateNumber)
    }

    fun getTotalVehicles(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _totalVehicles,
        coroutinesErrorHandler
    ) {
        vehicleRepository.getTotalVehicles()
    }

    fun getAllVehicles(coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _allVehiclesResponse,
        coroutinesErrorHandler
    ) {
            vehicleRepository.getAllVehicles()
    }

    fun insertVehicleAndEntryRecord(guestVehicle: VehicleInfo, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _insertEntryRecordResponse,
        coroutinesErrorHandler
    ) {
        vehicleRepository.insertVehicleAndEntryRecord(guestVehicle)
    }

    fun insertEntryRecord(plateNumber: String, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _insertEntryRecordResponse,
        coroutinesErrorHandler
    ) {
        vehicleRepository.insertEntryRecord(plateNumber)
    }
}
