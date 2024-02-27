package com.adzu.liecapp.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.adzu.liecapp.api.main.models.VehicleInfo
import com.adzu.liecapp.api.main.models.VehicleInfoResponse
import com.adzu.liecapp.presentation.viewmodels.VehicleViewModel
import com.adzu.liecapp.utils.ApiResponse

@Composable
fun VehicleInfoScreen(
    vehicleViewModel: VehicleViewModel = hiltViewModel()
) {
    val vehicleData by
    vehicleViewModel.vehicleInfoResponse.observeAsState(ApiResponse.Loading)

    when (vehicleData) {
        is ApiResponse.Loading -> {
            // Show loading indicator
//                CircularProgressIndicator()
            Text(text = "1")
        }
        is ApiResponse.Success -> {
            // Show list of vehicles
            val vehicleInfo = (vehicleData as ApiResponse.Success<VehicleInfoResponse>).data
            Text(
                text = vehicleInfo.vehicleInfo.toString(),
                modifier = Modifier.padding(16.dp)
            )
        }
        is ApiResponse.Failure -> {
            // Show error message
            val errorMessage = (vehicleData as ApiResponse.Failure).errorMessage
            Text(text = "Error: $errorMessage", modifier = Modifier.padding(16.dp))
        }
    }
}