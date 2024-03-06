package com.adzu.liecapp.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.adzu.liecapp.R
import com.adzu.liecapp.api.main.models.EntryCount
import com.adzu.liecapp.api.main.models.VehicleCount
import com.adzu.liecapp.api.main.models.VehicleInfo
import com.adzu.liecapp.presentation.viewmodels.CoroutinesErrorHandler
import com.adzu.liecapp.presentation.viewmodels.EntryRecordViewModel
import com.adzu.liecapp.presentation.viewmodels.VehicleViewModel
import com.adzu.liecapp.utils.ApiResponse
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    vehicleViewModel: VehicleViewModel = hiltViewModel(),
    recordViewModel: EntryRecordViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val pxValue = with(LocalDensity.current) { screenHeight.toPx() }
    var currentDateTime by remember { mutableStateOf(LocalDateTime.now()) }
    val statusEntry = remember { mutableStateOf("No Error found") }
    val statusVehicle = remember { mutableStateOf("No Error found") }

    val totalVehicle by
    vehicleViewModel.totalVehiclesResponse.observeAsState(ApiResponse.Loading)

    val totalEntry by
    recordViewModel.totalEntries.observeAsState(ApiResponse.Loading)


    // Coroutine to update the date and time every second
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // Delay for 1 second
            currentDateTime = LocalDateTime.now() // Update current date and time
        }
    }

    LaunchedEffect(key1 = true) {
        recordViewModel.getTotalEntryRecords(object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                statusEntry.value = "Error! $message"
            }
        })
    }

    LaunchedEffect(key1 = true) {
        vehicleViewModel.getTotalVehicles(object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                statusVehicle.value = "Error! $message"
            }
        })
    }


    val dateFormatter = DateTimeFormatter.ofPattern("EEEE - dd MMMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Blue, Color.Blue, Color.Black),
                startY = 0f,
                endY = pxValue, // Adjust the endY value as needed
            )
        )) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                )
            ){
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Hi! Welcome to LIECAP",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Very short description", // Replace with your actual description
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = currentDateTime.format(dateFormatter), // Display current date
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = currentDateTime.format(timeFormatter), // Display current time
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                content = {
                    // Heat stroke message card
                    item {
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .height(200.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                            ),
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.adzu_logo),
                                    contentDescription = null
                                )
                                Text(
                                    text = "LIECAP",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                    item {
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                            )
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ){
                                Text(
                                    text = "No of Vehicles",
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    style = MaterialTheme.typography.labelLarge
                                )
                                toDisplay(totalVehicle)
                            }
                        }
                    }
                    item {
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .height(180.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                            )
                        ) {
                            Text(
                                text = "Start Scanning",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                    item {
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                            )
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Entry Records",
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    style = MaterialTheme.typography.labelLarge
                                )
                                toDisplay(totalEntry)

                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun toDisplay(
    infoState: ApiResponse<*>
){
    when (infoState) {
        is ApiResponse.Loading -> {
            // Show loading indicator
//                CircularProgressIndicator()
            Image(
                painter = painterResource(id = R.drawable.paper),
                contentDescription = null,
            )
        }
        is ApiResponse.Success -> {
            // Show list of vehicles
            val response = (infoState as ApiResponse.Success<*>).data

            when (response) {
                is VehicleCount -> {
                    Text(
                        text = "Registered: ${response.total_valid_vehicles}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Expired: ${response.total_expired_vehicles}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Guests: ${response.total_guest_vehicles}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                is EntryCount -> {
                    // Handle response of type Int
                    Text(
                        text = "Total Entry: ${response.total_entry_records_today}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                else -> {
                    // Handle other types
                }
            }


        }
        is ApiResponse.Failure -> {
            // Show error message
            val errorMessage = (infoState as ApiResponse.Failure).errorMessage
            Text(text = "Error: $errorMessage", modifier = Modifier.padding(16.dp))
        }
    }
}