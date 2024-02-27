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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.adzu.liecapp.api.main.models.VehicleInfo
import com.adzu.liecapp.api.main.models.VehicleInfoResponse
import com.adzu.liecapp.presentation.components.CameraPreview
import com.adzu.liecapp.presentation.components.TextRecognitionAnalyzer
import com.adzu.liecapp.presentation.viewmodels.CoroutinesErrorHandler
import com.adzu.liecapp.presentation.viewmodels.VehicleViewModel
import com.adzu.liecapp.utils.ApiResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanningScreen(
    vehicleViewModel: VehicleViewModel = hiltViewModel()
) {
    val vehicleData by
    vehicleViewModel.vehicleInfoResponse.observeAsState(ApiResponse.Loading)

    var isModalOpen by remember { mutableStateOf(false) }
    var inputBoxText by remember { mutableStateOf("") }
    
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController = remember { LifecycleCameraController(context) }
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
        if(!isModalOpen) {
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
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}

@Composable
fun TransparentClipLayout(
    modifier: Modifier,
    width: Dp,
    height: Dp,
    offsetY: Dp,
) {
    val offsetInPx: Float
    val widthInPx: Float
    val heightInPx: Float

    with(LocalDensity.current) {
        offsetInPx = offsetY.toPx()
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

