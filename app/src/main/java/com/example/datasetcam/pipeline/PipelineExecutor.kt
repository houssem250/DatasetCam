package com.example.datasetcam.pipeline

import android.graphics.Bitmap

/**
 * Executes a list of ImageOperations in sequence on a captured Bitmap.
 */
class PipelineExecutor {

    /**
     * Run all operations serially.
     * - Iterate through operations list
     * - Pass output of each step as input to the next
     * - Return final transformed bitmap
     * - Recycle intermediate bitmaps to avoid memory leaks
     */
    fun execute(
        bitmap: Bitmap, 
        operations: List<ImageOperation>, 
        metadata: Map<String, Any>? = null
    ): Bitmap {
        var currentBitmap = bitmap
        for (operation in operations) {
            val nextBitmap = operation.apply(currentBitmap, metadata)
            
            // Recycle intermediate bitmap if it was newly created by the operation
            if (currentBitmap !== bitmap && currentBitmap !== nextBitmap && !currentBitmap.isRecycled) {
                currentBitmap.recycle()
            }
            
            currentBitmap = nextBitmap
        }
        return currentBitmap
    }
}
