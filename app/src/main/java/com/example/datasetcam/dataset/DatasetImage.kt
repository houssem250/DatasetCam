package com.example.datasetcam.dataset

/**
 * Represents one image in the dataset.
 */
data class DatasetImage(
    val filePath: String,       // absolute path
    val fileName: String,       // e.g. "class_cat_001.png"
    val label: String,          // e.g. "cat" (extracted from filename)
    val format: String,         // e.g. "png"
    val sizeBytes: Long,
    val width: Int,
    val height: Int,
    val metadata: Map<String, Any>?  // sensor metadata if available
)
