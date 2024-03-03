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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import com.adzu.liecapp.presentation.NavCons
import com.adzu.liecapp.presentation.viewmodels.CoroutinesErrorHandler
import com.adzu.liecapp.presentation.viewmodels.VehicleViewModel
import com.adzu.liecapp.utils.ApiResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleNotFound(
    viewModel: VehicleViewModel,
    navController: NavController,
    scannedPlate: String
){
    var owner by remember { mutableStateOf("") }
    var plateNumber by remember { mutableStateOf(scannedPlate) }
    var type by remember { mutableStateOf("") }
    val status = remember { mutableStateOf("No Error found") }
    var isQuerying by remember {mutableStateOf<Boolean>(false) }
    val vehicleData by
    viewModel.insertEntryRecordResponse.observeAsState(ApiResponse.Loading)

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
                text = "Vehicle Not Found!",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(64.dp))
            OutlinedTextField(
                value = owner,
                onValueChange = { owner = it },
                label = { Text(
                    text = "Owner",
                    color = Color.White
                ) },
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White, // Outline color when focused
                    unfocusedBorderColor = Color.White // Outline color when not focused
                ),
                shape = RoundedCornerShape(24.dp)
            )
            OutlinedTextField(
                value = plateNumber,
                onValueChange = { plateNumber = it },
                label = { Text(
                    text = "Plate Number",
                    color = Color.White
                ) },
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White, // Outline color when focused
                    unfocusedBorderColor = Color.White // Outline color when not focused
                ),
                shape = RoundedCornerShape(24.dp)
            )

            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text(
                    text = "Type",
                    color = Color.White
                ) },
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White, // Outline color when focused
                    unfocusedBorderColor = Color.White // Outline color when not focused
                ),
                shape = RoundedCornerShape(24.dp)
            )
            Spacer(modifier = Modifier.height(64.dp))
            Button(
                onClick = {
                    val newVehicle = VehicleInfo(
                        _id = null,
                        owner = owner,
                        plate_number = plateNumber,
                        validity = "Guest",
                        validity_date = null,
                        type = type
                    )
                    viewModel.insertVehicleAndEntryRecord(
                        newVehicle,
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
                    containerColor = Color.Green // Set button background color to green
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
                        text = "SAVE VEHICLE INFO",
                        fontSize = 18.sp
                    )
                }

            }

            when (vehicleData) {
                is ApiResponse.Loading -> {
                    //                CircularProgressIndicator(
                    //                    modifier = Modifier.width(64.dp),
                    //                    color = MaterialTheme.colorScheme.secondary,
                    //                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    //                )
                }

                is ApiResponse.Success -> {
                    val vehicleInfo = (vehicleData as ApiResponse.Success<*>).data

                    AlertDialog(
                        onDismissRequest = { },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    navController.navigate(NavCons.scan)
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
                                text = "Guest Vehicle Saved!",
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
    }
}