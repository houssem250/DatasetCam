package com.example.datasetcam.pipeline.operations

import android.graphics.Bitmap
import com.example.datasetcam.pipeline.ImageOperation
import com.example.datasetcam.storage.MetadataStore
import java.io.File
import java.io.FileOutputStream

/**
 * Terminal operation: saves the current bitmap to disk with optional metadata.
 * Returns the unmodified bitmap so the pipeline can continue if needed.
 */
class SaveOperation(
    private val format: String,     // "png" or "jpg"
    private val outputDir: String,
    private val label: String,
    private val metadata: Map<String, Any>? = null
) : ImageOperation {
    override val name = "Save ($format)"

    override fun apply(bitmap: Bitmap, metadata: Map<String, Any>?): Bitmap {
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
        
        try {
            FileOutputStream(file).use { out ->
                // Use 100 quality for datasets to avoid compression artifacts
                bitmap.compress(compressFormat, 100, out)
            }
            
            // Save companion metadata file if provided
            metadata?.let {
                MetadataStore.save(file.absolutePath, it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bitmap
    }
}
