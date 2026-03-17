package com.example.datasetcam.dataset

import android.content.Context
import com.example.datasetcam.storage.StorageManager
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Exports the dataset in various formats.
 */
class DatasetExporter(private val context: Context) {

    /** Export as ZIP archive including all images and a metadata.json summary */
    // OVERVIEW: create ZipOutputStream
    // OVERVIEW: for each image, add file entry
    // OVERVIEW: add metadata.json summary file
    // OVERVIEW: close zip
    fun exportZip(images: List<DatasetImage>, zipName: String): File? {
        val exportsDir = StorageManager.getExportsDir(context)
        val zipFile = File(exportsDir, if (zipName.endsWith(".zip")) zipName else "$zipName.zip")
        
        try {
            ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
                val metaArray = JSONArray()

                images.forEach { image ->
                    val file = File(image.filePath)
                    if (file.exists()) {
                        // Add image to ZIP
                        val entry = ZipEntry("images/${image.fileName}")
                        zos.putNextEntry(entry)
                        FileInputStream(file).use { it.copyTo(zos) }
                        zos.closeEntry()

                        // Add to metadata summary
                        val item = JSONObject().apply {
                            put("file", image.fileName)
                            put("label", image.label)
                            put("width", image.width)
                            put("height", image.height)
                            image.metadata?.forEach { (k, v) -> put(k, v) }
                        }
                        metaArray.put(item)
                    }
                }

                // Add metadata.json to ZIP
                val metaEntry = ZipEntry("metadata.json")
                zos.putNextEntry(metaEntry)
                zos.write(metaArray.toString(2).toByteArray())
                zos.closeEntry()
            }
            return zipFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /** Export in Kaggle-compatible folder structure: label/image.png */
    fun exportKaggle(images: List<DatasetImage>, folderName: String): File? {
        val exportsDir = File(StorageManager.getExportsDir(context), folderName)
        if (!exportsDir.exists()) exportsDir.mkdirs()

        try {
            images.forEach { image ->
                val labelDir = File(exportsDir, image.label)
                if (!labelDir.exists()) labelDir.mkdirs()
                
                val srcFile = File(image.filePath)
                val destFile = File(labelDir, image.fileName)
                if (srcFile.exists()) {
                    srcFile.copyTo(destFile, overwrite = true)
                }
            }
            return exportsDir
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /** Export in TensorFlow ImageFolder structure (train/test split could be added here) */
    fun exportTensorFlow(images: List<DatasetImage>, folderName: String): File? {
        // For now, this is identical to Kaggle structure (label subdirectories)
        return exportKaggle(images, folderName)
    }
}
