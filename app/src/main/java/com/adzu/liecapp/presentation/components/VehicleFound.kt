package com.adzu.liecapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.adzu.liecapp.api.main.models.VehicleInfo
import com.adzu.liecapp.presentation.viewmodels.CoroutinesErrorHandler
import com.adzu.liecapp.presentation.viewmodels.VehicleViewModel
import com.adzu.liecapp.utils.ApiResponse

@Composable
fun VehicleFound(
    viewModel: VehicleViewModel,
    vehicle: VehicleInfo,
    navController: NavController
){
    val status = remember { mutableStateOf("") }
    val vehicleData by
    viewModel.insertEntryRecordResponse.observeAsState(ApiResponse.Loading)
    var isQuerying by remember {mutableStateOf<Boolean>(false) }
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val pxValue = with(LocalDensity.current) { screenHeight.toPx() }

    Box(
        modifier = Modifier.fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Blue, Color.Blue, Color.Black),
                startY = 0f,
                endY = pxValue, // Adjust the endY value as needed
            )
        )
        .padding(32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = "Vehicle Information",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            VehicleData(vehicle)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    viewModel.insertEntryRecord(
                        vehicle.plate_number,
                        object : CoroutinesErrorHandler {
                            override fun onError(message: String) {
                                status.value = "Error! $message"
                            }
                        })
                    isQuerying = true
                },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green
                )
            ) {
                if (isQuerying) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(32.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                } else {
                    Text(
                        text = "SAVE VEHICLE ENTRY",
                        fontSize = 18.sp
                    )
                }
            }

            when (vehicleData) {
                is ApiResponse.Loading -> {
//                    CircularProgressIndicator(
//                        modifier = Modifier.width(64.dp),
//                        color = MaterialTheme.colorScheme.secondary,
//                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
//                    )
                }

                is ApiResponse.Success -> {
                    val vehicleInfo = (vehicleData as ApiResponse.Success<*>).data

                    AlertDialog(
                        onDismissRequest = { },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    navController.popBackStack()
                                },
                                shape = RoundedCornerShape(100.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(
                                    horizontal = 12.dp,
                                    vertical = 10.dp
                                ),
                                modifier = Modifier
                                    .requiredHeight(height = 40.dp)
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(
                                        8.dp,
                                        Alignment.CenterVertically
                                    ),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .requiredHeight(height = 40.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(
                                            8.dp,
                                            Alignment.CenterHorizontally
                                        ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        Text(
                                            text = "Done",
                                            color = Color(0xff6750a4),
                                            textAlign = TextAlign.Center,
                                            lineHeight = 1.43.em,
                                            style = MaterialTheme.typography.labelLarge,
                                            modifier = Modifier
                                                .wrapContentHeight(align = Alignment.CenterVertically)
                                        )
                                    }
                                }
                            }
                        },
                        title = {
                            Text(
                                text = "Success!",
                                color = Color(0xff1d1b20),
                                textAlign = TextAlign.Center,
                                lineHeight = 1.33.em,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .wrapContentHeight(align = Alignment.CenterVertically)
                            )
                        },
                        text = {
                            Text(
                                text = "Entry successfully uploaded to database!",
                                color = Color(0xff49454f),
                                lineHeight = 1.43.em,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp
                                ),
                                modifier = Modifier
                                    .wrapContentHeight(align = Alignment.CenterVertically)
                            )
                        },
                        icon = {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null)
                        },
                        containerColor = Color(0xffece6f0),
                        shape = RoundedCornerShape(28.dp),
                    )
                }

                is ApiResponse.Failure -> {
                    // Show error message
                    val errorMessage = (vehicleData as ApiResponse.Failure).errorMessage
                    status.value = errorMessage

                    AlertDialog(
                        onDismissRequest = { },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    isQuerying = false
                                },
                                shape = RoundedCornerShape(100.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(
                                    horizontal = 12.dp,
                                    vertical = 10.dp
                                ),
                                modifier = Modifier
                                    .requiredHeight(height = 40.dp)
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(
                                        8.dp,
                                        Alignment.CenterVertically
                                    ),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .requiredHeight(height = 40.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(
                                            8.dp,
                                            Alignment.CenterHorizontally
                                        ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        Text(
                                            text = "Close",
                                            color = Color(0xff6750a4),
                                            textAlign = TextAlign.Center,
                                            lineHeight = 1.43.em,
                                            style = MaterialTheme.typography.labelLarge,
                                            modifier = Modifier
                                                .wrapContentHeight(align = Alignment.CenterVertically)
                                        )
                                    }
                                }
                            }
                        },
                        title = {
                            Text(
                                text = "Something went wrong!",
                                color = Color(0xff1d1b20),
                                textAlign = TextAlign.Center,
                                lineHeight = 1.33.em,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .wrapContentHeight(align = Alignment.CenterVertically)
                            )
                        },
                        text = {
                            Text(
                                text = status.value,
                                color = Color.Red,
                                lineHeight = 1.43.em,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    letterSpacing = 0.25.sp
                                ),
                                modifier = Modifier
                                    .wrapContentHeight(align = Alignment.CenterVertically)
                            )
                        },
                        icon = {
                            Icon(imageVector = Icons.Default.Error, contentDescription = null)
                        },
                        containerColor = Color(0xffece6f0),
                        shape = RoundedCornerShape(28.dp),
                    )
                }
            }
        }

        if (status.value.contains("error", ignoreCase = true)) {
            ModalError(status.value)
        }
    }
}

@Composable
fun VehicleData(vehicle: VehicleInfo){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Owner: ${vehicle.owner}")
            Text(text = "Plate Number: ${vehicle.plate_number}")
            Text(text = "Validity: ${vehicle.validity}")
            Text(text = "Date: ${vehicle.validity_date}")
            Text(text = "Type: ${vehicle.type}")
        }
    }
}

@Composable
fun ModalError(status: String){
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = {
                },
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 10.dp
                ),
                modifier = Modifier
                    .requiredHeight(height = 40.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterVertically
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .requiredHeight(height = 40.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "Close",
                            color = Color(0xff6750a4),
                            textAlign = TextAlign.Center,
                            lineHeight = 1.43.em,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .wrapContentHeight(align = Alignment.CenterVertically)
                        )
                    }
                }
            }
        },
        title = {
            Text(
                text = "Something went wrong!",
                color = Color(0xff1d1b20),
                textAlign = TextAlign.Center,
                lineHeight = 1.33.em,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        },
        text = {
            Text(
                text = status,
                color = Color.Red,
                lineHeight = 1.43.em,
                style = TextStyle(
                    fontSize = 14.sp,
                    letterSpacing = 0.25.sp
                ),
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        },
        icon = {
            Icon(imageVector = Icons.Default.Error, contentDescription = null)
        },
        containerColor = Color(0xffece6f0),
        shape = RoundedCornerShape(28.dp),
    )
}