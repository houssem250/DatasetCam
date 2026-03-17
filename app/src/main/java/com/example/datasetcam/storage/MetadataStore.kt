package com.example.datasetcam.storage

import org.json.JSONObject
import java.io.File

/**
 * Reads/writes JSON metadata companion files alongside images.
 */
object MetadataStore {

    /** Save metadata as {imageName}.meta.json */
    fun save(imagePath: String, metadata: Map<String, Any>) {
        val metaPath = getMetaPath(imagePath)
        try {
            val json = JSONObject(metadata)
            File(metaPath).writeText(json.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Load metadata for an image, returns null if no companion file */
    fun load(imagePath: String): Map<String, Any>? {
        val metaPath = getMetaPath(imagePath)
        val file = File(metaPath)
        if (!file.exists()) return null

        return try {
            val json = JSONObject(file.readText())
            val map = mutableMapOf<String, Any>()
            val keys = json.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                map[key] = json.get(key)
            }
            map
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getMetaPath(imagePath: String): String {
        val lastDot = imagePath.lastIndexOf('.')
        return if (lastDot != -1) {
            imagePath.substring(0, lastDot) + ".meta.json"
        } else {
            "$imagePath.meta.json"
        }
    }
}
