package com.example.datasetcam.storage

/**
 * Reads/writes JSON metadata companion files alongside images.
 */
object MetadataStore {

    /** Save metadata as {imageName}.meta.json */
    fun save(imagePath: String, metadata: Map<String, Any>) {
        // TODO: derive meta path from image path (replace .png/.jpg with .meta.json)
        // TODO: convert map to JSONObject, write to file
    }

    /** Load metadata for an image, returns null if no companion file */
    fun load(imagePath: String): Map<String, Any>? {
        // TODO: derive meta path
        // TODO: check if .meta.json exists
        // TODO: read and parse JSONObject to map
        return null
    }
}
