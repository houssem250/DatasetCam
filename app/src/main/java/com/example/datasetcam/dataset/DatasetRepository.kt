package com.example.datasetcam.dataset

import android.content.Context
import android.graphics.Bitmap

/**
 * Scans dataset folder, provides image listing and metadata.
 */
class DatasetRepository(private val context: Context) {

    /** Scan dataset directory, return all DatasetImage entries */
    fun listImages(): List<DatasetImage> {
        // TODO: get dataset directory from StorageManager
        // TODO: list image files (.png, .jpg)
        // TODO: for each, read dimensions via BitmapFactory.Options (inJustDecodeBounds)
        // TODO: check for companion .json metadata file
        // TODO: parse label from filename pattern: {label}_{counter}.{ext}
        return emptyList()
    }

    /** Get grouped count by label */
    fun getLabelCounts(): Map<String, Int> {
        // TODO: group listImages() by label, count each
        return emptyMap()
    }

    /** Delete an image and its metadata */
    fun deleteImage(image: DatasetImage): Boolean {
        // TODO: delete image file + metadata .json if exists
        return false
    }

    /** Save a processed bitmap to the dataset folder */
    fun saveImage(
        bitmap: Bitmap,
        label: String,
        format: String,
        metadata: Map<String, Any>?
    ): DatasetImage? {
        // TODO: generate sequential filename: {label}_{nextCounter}.{format}
        // TODO: compress bitmap to file
        // TODO: save metadata as companion .json
        // TODO: return DatasetImage
        return null
    }
}
