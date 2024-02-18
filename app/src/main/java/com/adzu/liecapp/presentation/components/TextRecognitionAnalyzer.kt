package com.adzu.liecapp.presentation.components

import android.graphics.ImageFormat
import android.graphics.Rect
import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
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
            val mediaImage: Image = imageProxy.image ?: run {
                imageProxy.close()
                return@launch
            }
            val cropRect = calculateCropRect(imageProxy.width, imageProxy.height)
            imageProxy.setCropRect(cropRect)
            suspendCoroutine { continuation ->
                if (mediaImage != null && mediaImage.format == ImageFormat.YUV_420_888) {
                    print("media is format YUV")
                    croppedNV21(mediaImage, imageProxy.cropRect).let {byteArray ->
                        textRecognizer.process(
                            InputImage.fromByteArray(
                                byteArray,
                                imageProxy.cropRect.width(),
                                imageProxy.cropRect.height(),
                                imageProxy.imageInfo.rotationDegrees,
                                IMAGE_FORMAT_NV21
                            )
                        ).addOnSuccessListener { visionText: Text ->
                            val detectedText: String = visionText.text
                            if (detectedText.isNotBlank()) {
                                onDetectedTextUpdated(detectedText)
                            }
                        }
                            .addOnCompleteListener {
                                continuation.resume(Unit)
                            }
                    }

                }
                print("Not image")
            }

            delay(THROTTLE_TIMEOUT_MS)
        }.invokeOnCompletion { exception ->
            exception?.printStackTrace()
            imageProxy.close()
        }
    }

    private fun croppedNV21(mediaImage: Image, cropRect: Rect): ByteArray {
        val yBuffer = mediaImage.planes[0].buffer // Y
        val vuBuffer = mediaImage.planes[2].buffer // VU

        val ySize = yBuffer.remaining()
        val vuSize = vuBuffer.remaining()

        val nv21 = ByteArray(ySize + vuSize)

        yBuffer.get(nv21, 0, ySize)
        vuBuffer.get(nv21, ySize, vuSize)

        return cropByteArray(nv21, mediaImage.width, cropRect)
    }

    private fun cropByteArray(array: ByteArray, imageWidth: Int, cropRect: Rect): ByteArray {
        val croppedArray = ByteArray(cropRect.width() * cropRect.height())
        var i = 0
        array.forEachIndexed { index, byte ->
            val x = index % imageWidth
            val y = index / imageWidth

            if (cropRect.left <= x && x < cropRect.right && cropRect.top <= y && y < cropRect.bottom) {
                croppedArray[i] = byte
                i++
            }
        }
        return croppedArray
    }

    private fun calculateCropRect(imageWidth: Int, imageHeight: Int): Rect {
        // Calculate the dimensions of the region of focus (rectangle/frame in the middle)
        val focusRegionWidth = 0.5 * imageWidth // For example, half of the image width
        val focusRegionHeight = 0.3 * imageHeight // For example, 30% of the image height
        val focusRegionLeft = (imageWidth - focusRegionWidth) / 2
        val focusRegionTop = (imageHeight - focusRegionHeight) / 2
        val focusRegionRight = focusRegionLeft + focusRegionWidth
        val focusRegionBottom = focusRegionTop + focusRegionHeight

        // Create a Rect object to define the cropping region
        return Rect(focusRegionLeft.toInt(), focusRegionTop.toInt(), focusRegionRight.toInt(), focusRegionBottom.toInt())
    }
}