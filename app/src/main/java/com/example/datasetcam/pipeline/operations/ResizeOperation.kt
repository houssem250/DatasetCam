package com.example.datasetcam.pipeline.operations

import android.graphics.Bitmap
import com.example.datasetcam.pipeline.ImageOperation

/**
 * Scales a bitmap to the specified width and height.
 */
class ResizeOperation(private val width: Int, private val height: Int) : ImageOperation {
    override val name = "Resize (${width}x${height})"

    override fun apply(bitmap: Bitmap): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }
}
