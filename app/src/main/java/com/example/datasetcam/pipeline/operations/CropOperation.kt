package com.example.datasetcam.pipeline.operations

import android.graphics.Bitmap
import com.example.datasetcam.pipeline.ImageOperation

/**
 * Crops a specific region from the bitmap.
 */
class CropOperation(
    private val x: Int, private val y: Int,
    private val cropWidth: Int, private val cropHeight: Int
) : ImageOperation {
    override val name = "Crop (${cropWidth}x${cropHeight})"

    override fun apply(bitmap: Bitmap, metadata: Map<String, Any>?): Bitmap {
        // Ensure bounds are safe
        val safeX = kotlin.math.max(0, x)
        val safeY = kotlin.math.max(0, y)
        val safeWidth = kotlin.math.min(cropWidth, bitmap.width - safeX)
        val safeHeight = kotlin.math.min(cropHeight, bitmap.height - safeY)

        if (safeWidth <= 0 || safeHeight <= 0) {
            return bitmap // Invalid crop bounds, return original
        }

        return Bitmap.createBitmap(bitmap, safeX, safeY, safeWidth, safeHeight)
    }
}
