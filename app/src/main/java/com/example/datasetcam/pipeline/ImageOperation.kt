package com.example.datasetcam.pipeline

import android.graphics.Bitmap

/**
 * Contract for all image processing steps.
 * Each operation takes a Bitmap and returns a transformed Bitmap.
 */
interface ImageOperation {
    /** Human-readable name for UI display */
    val name: String

    /** Apply transformation to the input bitmap, return new bitmap */
    fun apply(bitmap: Bitmap): Bitmap
}
