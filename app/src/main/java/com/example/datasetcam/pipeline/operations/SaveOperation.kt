package com.example.datasetcam.pipeline.operations

import android.graphics.Bitmap
import com.example.datasetcam.pipeline.ImageOperation
import java.io.File
import java.io.FileOutputStream

/**
 * Terminal operation: saves the current bitmap to disk.
 * Returns the unmodified bitmap so the pipeline can continue if needed.
 */
class SaveOperation(
    private val format: String,     // "png" or "jpg"
    private val outputDir: String,
    private val label: String
) : ImageOperation {
    override val name = "Save ($format)"

    override fun apply(bitmap: Bitmap): Bitmap {
        // Overview: generate filename as {label}_{counter}.{format}
        // Overview: compress bitmap to file using Bitmap.compress()
        
        val dir = File(outputDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val compressFormat = if (format.lowercase() == "jpg" || format.lowercase() == "jpeg") {
            Bitmap.CompressFormat.JPEG
        } else {
            Bitmap.CompressFormat.PNG
        }

        val suffix = if (compressFormat == Bitmap.CompressFormat.JPEG) ".jpg" else ".png"
        val timestamp = System.currentTimeMillis()
        val fileName = "${label}_${timestamp}$suffix"
        
        val file = File(dir, fileName)
        
        FileOutputStream(file).use { out ->
            // Use 100 quality for datasets to avoid compression artifacts
            bitmap.compress(compressFormat, 100, out)
        }

        return bitmap
    }
}
