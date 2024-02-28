package com.adzu.liecapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.adzu.liecapp.api.main.models.VehicleInfo
import com.adzu.liecapp.presentation.viewmodels.CoroutinesErrorHandler
import com.adzu.liecapp.presentation.viewmodels.VehicleViewModel
import com.adzu.liecapp.utils.ApiResponse

@Composable
fun VehiclesScreen(
    vehicleViewModel: VehicleViewModel = hiltViewModel()
) {
    val vehicleInfoState by
    vehicleViewModel.allVehiclesResponse.observeAsState(ApiResponse.Loading)
    val lifecycleOwner = LocalLifecycleOwner.current
    val status = remember { mutableStateOf("No Error found") }

    // Fetch list of vehicles when the screen is first shown
    LaunchedEffect(key1 = true) {
        vehicleViewModel.getAllVehicles(object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                status.value = "Error! $message"
            }
        })
    }

    Surface(modifier = Modifier.fillMaxSize().padding(bottom = 16.dp)) {
//        Text( text = status.value)
        when (vehicleInfoState) {
            is ApiResponse.Loading -> {
                // Show loading indicator
//                CircularProgressIndicator()
                Text(text = "1")
            }
            is ApiResponse.Success -> {
                // Show list of vehicles
                val vehicles = (vehicleInfoState as ApiResponse.Success<List<VehicleInfo>>).data
                VehiclesList(vehicles = vehicles)
            }
            is ApiResponse.Failure -> {
                // Show error message
                val errorMessage = (vehicleInfoState as ApiResponse.Failure).errorMessage
                Text(text = "Error: $errorMessage", modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun VehiclesList(vehicles: List<VehicleInfo>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1), // 2 columns
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(vehicles) { vehicle ->
            VehicleListItem(vehicle = vehicle)
        }
    }
}

@Composable
fun VehicleListItem(vehicle: VehicleInfo) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Owner: ${vehicle.owner}")
            Text(text = "Plate Number: ${vehicle.plate_number}")
            Text(text = "Validity: ${vehicle.validity}")
            Text(text = "Date: ${vehicle.validity_date}")
            Text(text = "Type: ${vehicle.type}")
            // Add more text fields for other vehicle properties as needed
        }
    }
}
