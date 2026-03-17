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
    fun execute(bitmap: Bitmap, operations: List<ImageOperation>): Bitmap {
        var currentBitmap = bitmap
        for (operation in operations) {
            val nextBitmap = operation.apply(currentBitmap)
            // TODO: recycle currentBitmap if it's different from nextBitmap to avoid OOM
            currentBitmap = nextBitmap
        }
        return currentBitmap
    }
}
