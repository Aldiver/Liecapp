package com.adzu.liecapp.presentation.components

import android.graphics.ImageFormat
import android.graphics.Rect
import android.media.Image
import android.util.Log
import android.view.Surface
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.adzu.liecapp.utils.camera.centerCrop
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.common.InputImage.IMAGE_FORMAT_NV21
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TextRecognitionAnalyzer(
    private val onDetectedTextUpdated: (String) -> Unit
) : ImageAnalysis.Analyzer {

    companion object {
        const val THROTTLE_TIMEOUT_MS = 1_000L
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val textRecognizer: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
//            val mediaImage: Image = imageProxy.image ?: run { imageProxy.close(); return@launch }
//            val inputImage: InputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val bitmap = imageProxy
                .toBitmap()
                .centerCrop(150, 300)

            // Calculate the device orientation
            val deviceOrientation = when (imageProxy.imageInfo.rotationDegrees) {
                0 -> Surface.ROTATION_0
                90 -> Surface.ROTATION_90
                180 -> Surface.ROTATION_180
                270 -> Surface.ROTATION_270
                else -> Surface.ROTATION_0
            }

            // Calculate the adjustment needed for landscape orientation
            val rotationAdjustment = when (deviceOrientation) {
                Surface.ROTATION_0 -> 0
                Surface.ROTATION_90 -> 270
                Surface.ROTATION_180 -> 180
                Surface.ROTATION_270 -> 90
                else -> 0
            }

            val adjustedRotationDegrees = (rotationDegrees + rotationAdjustment) % 360

            Log.d("data ", adjustedRotationDegrees.toString() )
            suspendCoroutine { continuation ->
                textRecognizer.process(InputImage.fromBitmap(bitmap, adjustedRotationDegrees))
                    .addOnSuccessListener { visionText: Text ->
                        val detectedText: String = visionText.text
                        if (detectedText.isNotBlank()) {
                            onDetectedTextUpdated(detectedText)
                        }
                    }
                    .addOnCompleteListener {
                        continuation.resume(Unit)
                    }
            }

            delay(THROTTLE_TIMEOUT_MS)
        }.invokeOnCompletion { exception ->
            exception?.printStackTrace()
            imageProxy.close()
        }
    }
}