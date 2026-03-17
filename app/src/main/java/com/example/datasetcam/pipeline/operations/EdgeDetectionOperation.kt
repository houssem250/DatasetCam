package com.example.datasetcam.pipeline.operations

import android.graphics.Bitmap
import android.graphics.Color
import com.example.datasetcam.pipeline.ImageOperation
import kotlin.math.abs

/**
 * Applies edge detection (e.g. Sobel, Canny) to the image.
 * Applies edge detection using a simple Sobel operator.
 * For production, consider using OpenCV, but this provides a baseline pure-Kotlin implementation.
 */
class EdgeDetectionOperation : ImageOperation {
    override val name = "Edge Detection"

    override fun apply(bitmap: Bitmap): Bitmap {
        // Overview: convert to grayscale, apply Sobel/Canny convolution kernel
        
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        
        val resultPixels = IntArray(width * height)

        // Sobel kernels
        val gx = intArrayOf(-1, 0, 1, -2, 0, 2, -1, 0, 1)
        val gy = intArrayOf(1, 2, 1, 0, 0, 0, -1, -2, -1)

        // Pre-compute grayscale values to speed up convolution
        val grayPixels = IntArray(width * height)
        for (i in pixels.indices) {
            val color = pixels[i]
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)
            grayPixels[i] = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
        }

        // Apply convolution (ignoring 1px border)
        for (y in 1 until height - 1) {
            for (x in 1 until width - 1) {
                var sumX = 0
                var sumY = 0

                var k = 0
                for (ky in -1..1) {
                    for (kx in -1..1) {
                        val pixelIdx = (y + ky) * width + (x + kx)
                        val gray = grayPixels[pixelIdx]
                        sumX += gray * gx[k]
                        sumY += gray * gy[k]
                        k++
                    }
                }

                var magnitude = abs(sumX) + abs(sumY)
                if (magnitude > 255) magnitude = 255
                
                resultPixels[y * width + x] = Color.rgb(magnitude, magnitude, magnitude)
            }
        }

        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        resultBitmap.setPixels(resultPixels, 0, width, 0, 0, width, height)
        return resultBitmap
    }
}
