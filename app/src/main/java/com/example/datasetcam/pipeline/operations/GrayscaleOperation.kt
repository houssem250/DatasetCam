package com.example.datasetcam.pipeline.operations

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.example.datasetcam.pipeline.ImageOperation

/**
 * Converts a bitmap to grayscale using a ColorMatrix.
 */
class GrayscaleOperation : ImageOperation {
    override val name = "Grayscale"

    override fun apply(bitmap: Bitmap, metadata: Map<String, Any>?): Bitmap {
        // Overview: use ColorMatrix with saturation=0, apply via Canvas + Paint
        
        val grayscaleBitmap = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height,
            Bitmap.Config.ARGB_8888
        )
        
        val canvas = Canvas(grayscaleBitmap)
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        
        return grayscaleBitmap
    }
}
