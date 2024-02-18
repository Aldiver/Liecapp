package com.adzu.liecapp.presentation.components

import android.content.Context
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.RectF
import android.media.Image
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.OptIn
import androidx.camera.core.AspectRatio
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

@Composable
fun CameraScreen() {
    CameraContent()
}

@OptIn(ExperimentalGetImage::class)
@Composable
private fun CameraContent() {

    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController = remember { LifecycleCameraController(context) }
    var detectedText: String by remember { mutableStateOf("No text detected yet..") }

    fun onTextUpdated(updatedText: String) {
        detectedText = updatedText
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
    ) { paddingValues: PaddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                color = androidx.compose.ui.graphics.Color.Black,
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize()
                        .padding(paddingValues),
                    factory = { context ->
                        PreviewView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            setBackgroundColor(Color.BLACK)
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            scaleType = PreviewView.ScaleType.FILL_START
                        }.also { previewView ->
                            startTextRecognition(
                                context = context,
                                cameraController = cameraController,
                                lifecycleOwner = lifecycleOwner,
                                previewView = previewView,
                                onDetectedTextUpdated = ::onTextUpdated
                            )
                        }
                    }
                )
                Text(
                    text = detectedText,
                    textAlign = TextAlign.Center,
                    color = androidx.compose.ui.graphics.Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .background(androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f))
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

private fun startTextRecognition(
    context: Context,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onDetectedTextUpdated: (String) -> Unit
) {

    cameraController.imageAnalysisTargetSize = CameraController.OutputSize(AspectRatio.RATIO_16_9)
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        TextRecognitionAnalyzer(onDetectedTextUpdated = onDetectedTextUpdated)
    )

    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}