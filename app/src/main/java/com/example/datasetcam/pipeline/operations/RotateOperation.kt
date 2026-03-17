package com.example.datasetcam.pipeline.operations

import android.graphics.Bitmap
import android.graphics.Matrix
import com.example.datasetcam.pipeline.ImageOperation

/**
 * Rotates a bitmap by the specified degrees.
 */
class RotateOperation(private val degrees: Float) : ImageOperation {
    override val name = "Rotate (${degrees}°)"

    override fun apply(bitmap: Bitmap): Bitmap {
        // Overview: use Matrix.postRotate, Bitmap.createBitmap with matrix

        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )
    }
}
