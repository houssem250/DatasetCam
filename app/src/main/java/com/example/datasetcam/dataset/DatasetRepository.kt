package com.example.datasetcam.dataset

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.datasetcam.storage.MetadataStore
import com.example.datasetcam.storage.StorageManager
import java.io.File
import java.io.FileOutputStream

/**
 * Scans dataset folder, provides image listing and metadata.
 */
class DatasetRepository(private val context: Context) {

    /** Scan dataset directory, return all DatasetImage entries */
    fun listImages(): List<DatasetImage> {
        val dir = StorageManager.getDatasetDir(context)
        val files = dir.listFiles { _, name -> 
            name.endsWith(".png", ignoreCase = true) || name.endsWith(".jpg", ignoreCase = true) || name.endsWith(".jpeg", ignoreCase = true) 
        } ?: return emptyList()

        return files.mapNotNull { file ->
            try {
                val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeFile(file.absolutePath, options)
                
                val fileName = file.name
                val label = fileName.substringBefore("_")
                val format = fileName.substringAfterLast(".")
                val metadata = MetadataStore.load(file.absolutePath)

                DatasetImage(
                    filePath = file.absolutePath,
                    fileName = fileName,
                    label = label,
                    format = format,
                    sizeBytes = file.length(),
                    width = options.outWidth,
                    height = options.outHeight,
                    metadata = metadata
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }.sortedByDescending { it.fileName }
    }

    /** Get grouped count by label */
    fun getLabelCounts(): Map<String, Int> {
        return listImages().groupBy { it.label }.mapValues { it.value.size }
    }

    /** Delete an image and its metadata */
    fun deleteImage(image: DatasetImage): Boolean {
        val imageFile = File(image.filePath)
        val metaFile = File(image.filePath.substringBeforeLast(".") + ".meta.json")
        
        var deleted = false
        if (imageFile.exists()) {
            deleted = imageFile.delete()
        }
        if (metaFile.exists()) {
            metaFile.delete()
        }
        return deleted
    }

    /** Save a processed bitmap to the dataset folder manually (outside the pipeline) */
    fun saveImage(
        bitmap: Bitmap,
        label: String,
        format: String,
        metadata: Map<String, Any>?
    ): DatasetImage? {
        val dir = StorageManager.getDatasetDir(context)
        val timestamp = System.currentTimeMillis()
        val extension = if (format.lowercase() == "jpg" || format.lowercase() == "jpeg") "jpg" else "png"
        val fileName = "${label}_${timestamp}.$extension"
        val file = File(dir, fileName)

        try {
            FileOutputStream(file).use { out ->
                val compressFormat = if (extension == "jpg") Bitmap.CompressFormat.JPEG else Bitmap.CompressFormat.PNG
                bitmap.compress(compressFormat, 100, out)
            }
            
            if (metadata != null) {
                MetadataStore.save(file.absolutePath, metadata)
            }

            return DatasetImage(
                filePath = file.absolutePath,
                fileName = fileName,
                label = label,
                format = extension,
                sizeBytes = file.length(),
                width = bitmap.width,
                height = bitmap.height,
                metadata = metadata
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
