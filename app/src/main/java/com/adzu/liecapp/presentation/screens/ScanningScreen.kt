package com.adzu.liecapp.presentation.screens

import android.content.Context
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.sharp.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.adzu.liecapp.api.main.models.VehicleInfo
import com.adzu.liecapp.api.main.models.VehicleInfoResponse
import com.adzu.liecapp.presentation.NavCons
import com.adzu.liecapp.presentation.components.CameraPreview
import com.adzu.liecapp.presentation.components.TextRecognitionAnalyzer
import com.adzu.liecapp.presentation.viewmodels.CoroutinesErrorHandler
import com.adzu.liecapp.presentation.viewmodels.VehicleViewModel
import com.adzu.liecapp.utils.ApiResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanningScreen(
    vehicleViewModel: VehicleViewModel = hiltViewModel(),
    navHostController: NavController
) {
    val vehicleData by
    vehicleViewModel.vehicleInfoResponse.observeAsState(ApiResponse.Loading)

    var isModalOpen by remember { mutableStateOf(false) }
    var isScanning by remember { mutableStateOf(false) }
    var inputBoxText by remember { mutableStateOf("") }
    
    val context: Context = LocalContext.current
    var detectedText: String by remember { mutableStateOf("No text detected yet..") }
    val status = remember { mutableStateOf("No Error found") }

    fun onTextUpdated(updatedText: String) {
        detectedText = updatedText
    }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                TextRecognitionAnalyzer(onDetectedTextUpdated = ::onTextUpdated)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if(!isModalOpen && !isScanning) {
            CameraPreview(controller, Modifier.fillMaxSize())

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                    Text(
                        text = detectedText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
            }

            TransparentClipLayout(
                modifier = Modifier.fillMaxSize(),
                width = 300.dp,
                height = 150.dp,
                offsetY = 150.dp,
            )

            Button(
                onClick = {
                    inputBoxText = detectedText
                    isModalOpen = true
                },
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomCenter) // Align the Button to the bottom center of its parent
                    .offset(y = (-200).dp) // Set the y-offset to move the Button up by 300 dp
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }

        if (isModalOpen) {
            AlertDialog(
                onDismissRequest = { },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Perform action on check button click
                            vehicleViewModel.getVehicleInfo(inputBoxText, object : CoroutinesErrorHandler {
                                override fun onError(message: String) {
                                    // Handle error
                                    status.value = "Error! $message"
                                }
                            })
                            //check
                            isScanning = true
                            isModalOpen = false


                        },
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                        modifier = Modifier
                            .requiredHeight(height = 40.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .requiredHeight(height = 40.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Text(
                                    text = "Search",
                                    color = Color(0xff6750a4),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 1.43.em,
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier
                                        .wrapContentHeight(align = Alignment.CenterVertically))
                            }
                        }
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { isModalOpen = false },
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                        modifier = Modifier
                            .requiredHeight(height = 40.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .requiredHeight(height = 40.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Text(
                                    text = "Cancel",
                                    color = Color(0xff6750a4),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 1.43.em,
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier
                                        .wrapContentHeight(align = Alignment.CenterVertically))
                            }
                        }
                    }
                },
                title = {
                    Text(
                        text = "Search Vehicle Plate Number",
                        color = Color(0xff1d1b20),
                        lineHeight = 1.33.em,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .wrapContentHeight(align = Alignment.CenterVertically))
                },
                text = {
                    TextField(
                        value = inputBoxText,
                        onValueChange = { inputBoxText = it },
                        label = { Text("Input Box") },
                        modifier = Modifier
                            .wrapContentHeight(align = Alignment.CenterVertically)
                            )
                },
                containerColor = Color(0xffece6f0),
                shape = RoundedCornerShape(28.dp),
                )
        }

        if(isScanning){
            when (vehicleData) {
                is ApiResponse.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }

                is ApiResponse.Success -> {
                    val vehicleInfo = (vehicleData as ApiResponse.Success<VehicleInfoResponse>).data
                    VehicleFound(viewModel = vehicleViewModel,vehicle = vehicleInfo.vehicleInfo, navController = navHostController)

                }

                is ApiResponse.Failure -> {
                    // Show error message
                    val errorMessage = (vehicleData as ApiResponse.Failure).errorMessage
                    VehicleNotFound(viewModel = vehicleViewModel, navController = navHostController)
                }
            }
        }
    }
}

@Composable
fun VehicleFound(
    viewModel: VehicleViewModel,
    vehicle: VehicleInfo,
    navController: NavController
){
    val status = remember { mutableStateOf("No Error found") }
    val vehicleData by
    viewModel.insertEntryRecordResponse.observeAsState(ApiResponse.Loading)
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ){
        Text(
            text = "Owner: ${vehicle.owner}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Plate Number: ${vehicle.plate_number}",
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Validity: ${vehicle.validity}",
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Validity Date: ${vehicle.validity_date}",
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Type: ${vehicle.type}",
            fontSize = 18.sp
        )

        Button(
            onClick = {
                viewModel.insertEntryRecord(vehicle.plate_number, object : CoroutinesErrorHandler {
                    override fun onError(message: String) {
                        status.value = "Error! $message"
                    }
                })
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Save Entry!")
        }

        when (vehicleData) {
            is ApiResponse.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
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
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                            modifier = Modifier
                                .requiredHeight(height = 40.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .requiredHeight(height = 40.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
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
                                            .wrapContentHeight(align = Alignment.CenterVertically))
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
                                .wrapContentHeight(align = Alignment.CenterVertically))
                    },
                    text = {
                        Text(
                            text = "Entry successfully uploaded to database!",
                            color = Color(0xff49454f),
                            lineHeight = 1.43.em,
                            style = TextStyle(
                                fontSize = 14.sp,
                                letterSpacing = 0.25.sp),
                            modifier = Modifier
                                .wrapContentHeight(align = Alignment.CenterVertically))
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
            }
        }
    }
}

@Composable
fun VehicleNotFound(
    viewModel: VehicleViewModel,
    navController: NavController
    ){
    var owner by remember { mutableStateOf("") }
    var plateNumber by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    val status = remember { mutableStateOf("No Error found") }
    val vehicleData by
    viewModel.insertEntryRecordResponse.observeAsState(ApiResponse.Loading)


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ){
        Text(
            style = MaterialTheme.typography.displayMedium,
            text = "Vehicle Not Found!",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = owner,
            onValueChange = { owner = it },
            label = { Text("Owner") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = plateNumber,
            onValueChange = { plateNumber = it },
            label = { Text("Plate Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Type") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
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
                viewModel.insertVehicleAndEntryRecord(newVehicle, object : CoroutinesErrorHandler {
                    override fun onError(message: String) {
                        status.value = "Error! $message"
                    }
                })
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Save Vehicle Info")
        }

        when (vehicleData) {
            is ApiResponse.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            is ApiResponse.Success -> {
                val vehicleInfo = (vehicleData as ApiResponse.Success<*>).data

                AlertDialog(
                    onDismissRequest = { },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                navController.navigate(NavCons.scan)
                                      },
                            shape = RoundedCornerShape(100.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                            modifier = Modifier
                                .requiredHeight(height = 40.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .requiredHeight(height = 40.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
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
                                            .wrapContentHeight(align = Alignment.CenterVertically))
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
                                .wrapContentHeight(align = Alignment.CenterVertically))
                    },
                    text = {
                        Text(
                            text = "Entry successfully uploaded to database!",
                            color = Color(0xff49454f),
                            lineHeight = 1.43.em,
                            style = TextStyle(
                                fontSize = 14.sp,
                                letterSpacing = 0.25.sp),
                            modifier = Modifier
                                .wrapContentHeight(align = Alignment.CenterVertically))
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
            }
        }
        Text(
            text = status.value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun TransparentClipLayout(
    modifier: Modifier,
    width: Dp,
    height: Dp,
    offsetY: Dp,
) {
    val widthInPx: Float
    val heightInPx: Float

    with(LocalDensity.current) {
        widthInPx = width.toPx()
        heightInPx = height.toPx()
    }

    Canvas(modifier = modifier) {

        val canvasWidth = size.width
        val canvasHeight = size.height

        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)

            // Destination
            drawRect(Color(0x77000000))

            // Source
            drawRoundRect(
                topLeft = Offset(
                    x = (canvasWidth - widthInPx) / 2,
                    y = (canvasHeight - heightInPx) / 2
                ),
                size = Size(widthInPx, heightInPx),
                cornerRadius = CornerRadius(30f,30f),
                color = Color.Transparent,
                blendMode = BlendMode.Clear
            )
            restoreToCount(checkPoint)
        }
    }
}

