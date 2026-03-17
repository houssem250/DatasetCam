package com.example.datasetcam.storage

import android.content.Context
import java.io.File

/**
 * Manages app-specific directories for scripts, datasets, exports.
 */
object StorageManager {

    /** Get or create the scripts directory */
    fun getScriptsDir(context: Context): File {
        val dir = File(context.getExternalFilesDir(null), "scripts")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    /** Get or create the dataset output directory */
    fun getDatasetDir(context: Context): File {
        val dir = File(context.getExternalFilesDir(null), "dataset")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    /** Get or create the exports directory */
    fun getExportsDir(context: Context): File {
        val dir = File(context.getExternalFilesDir(null), "exports")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }
}
