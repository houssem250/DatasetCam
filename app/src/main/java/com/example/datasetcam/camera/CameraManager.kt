package com.example.datasetcam.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.media.Image
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Wraps CameraX setup: preview, image capture, lifecycle binding.
 */
class CameraManager(private val context: Context) {

    // Overview: use CameraX (Preview + ImageCapture use cases)

    private var imageCapture: ImageCapture? = null
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var cameraProvider: ProcessCameraProvider? = null

    /** Bind CameraX preview + capture use cases to lifecycle */
    fun startCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(context))
    }

    /** Capture a single frame, convert ImageProxy → Bitmap, invoke callback */
    fun captureImage(onCaptured: (Bitmap?) -> Unit) {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(
            cameraExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val bitmap = imageProxyToBitmap(image)
                    image.close()
                    onCaptured(bitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    onCaptured(null)
                }
            }
        )
    }

    /** Convert ImageProxy to Bitmap handling rotation */
    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        // Overview: convert ImageProxy → Bitmap (YUV → RGB) + apply EXIF rotation correction

        val planeProxy = image.planes[0]
        val buffer: ByteBuffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        // Rotate if needed
        val rotationDegrees = image.imageInfo.rotationDegrees.toFloat()
        if (rotationDegrees != 0f) {
            val matrix = Matrix()
            matrix.postRotate(rotationDegrees)
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
        return bitmap
    }

    /** Release camera resources */
    fun shutdown() {
        // Overview: unbind all use cases, shutdown executor

        cameraProvider?.unbindAll()
        cameraExecutor.shutdown()
    }
}
