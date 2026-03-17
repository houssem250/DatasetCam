package com.example.datasetcam.pipeline.operations

import android.graphics.Bitmap
import android.graphics.Color
import com.example.datasetcam.pipeline.ImageOperation

/**
 * Normalizes pixel values across the entire image to span the full 0-255 range.
 * This implementation assumes the image is already grayscale (or normalizes RGB channels together).
 */
class NormalizeOperation : ImageOperation {
    override val name = "Normalize"

    override fun apply(bitmap: Bitmap, metadata: Map<String, Any>?): Bitmap {
        // Overview: read all pixels, find min/max, scale to 0-255 range

        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        var minIntensity = 255
        var maxIntensity = 0

        // Find min and max intensity (using the Red channel, assuming grayscale input)
        for (pixel in pixels) {
            val intensity = Color.red(pixel)
            if (intensity < minIntensity) minIntensity = intensity
            if (intensity > maxIntensity) maxIntensity = intensity
        }

        if (minIntensity == maxIntensity) {
            return bitmap // Avoid division by zero, image is a solid color
        }

        val range = maxIntensity - minIntensity
        
        // Normalize each pixel
        val normalizedPixels = IntArray(width * height)
        for (i in pixels.indices) {
            val pixel = pixels[i]
            val r = Color.red(pixel)
            val g = Color.green(pixel)
            val b = Color.blue(pixel)
            val a = Color.alpha(pixel)

            val newR = ((r - minIntensity) * 255f / range).toInt()
            val newG = ((g - minIntensity) * 255f / range).toInt()
            val newB = ((b - minIntensity) * 255f / range).toInt()

            normalizedPixels[i] = Color.argb(a, newR, newG, newB)
        }

        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        resultBitmap.setPixels(normalizedPixels, 0, width, 0, 0, width, height)
        return resultBitmap
    }
}
