package com.example.datasetcam.dataset

import android.content.Context

/**
 * Exports the dataset in various formats.
 */
class DatasetExporter(private val context: Context) {

    /** Export as ZIP archive */
    fun exportZip(images: List<DatasetImage>, outputPath: String) {
        // TODO: create ZipOutputStream
        // TODO: for each image, add file entry
        // TODO: add metadata.json summary file
        // TODO: close zip
    }

    /** Export in Kaggle-compatible folder structure: label/image.png */
    fun exportKaggle(images: List<DatasetImage>, outputPath: String) {
        // TODO: create label subdirectories
        // TODO: copy images into their label folder
        // TODO: generate CSV manifest if needed
    }

    /** Export in TensorFlow ImageFolder structure */
    fun exportTensorFlow(images: List<DatasetImage>, outputPath: String) {
        // TODO: create train/test split
        // TODO: organize into label subdirectories
        // TODO: generate labels.txt
    }
}
