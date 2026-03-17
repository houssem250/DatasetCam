package com.example.datasetcam.dataset

import androidx.compose.runtime.Composable

@Composable
fun DatasetManagerScreen(
    images: List<DatasetImage>,
    labelCounts: Map<String, Int>,
    onImageClick: (DatasetImage) -> Unit,
    onExportClick: () -> Unit,
    onDeleteImage: (DatasetImage) -> Unit
) {
    // TODO: TopAppBar with "Dataset Manager" + export button
    // TODO: Summary chips showing label counts (e.g. "cat: 12", "dog: 8")
    // TODO: LazyVerticalGrid showing image thumbnails
    // TODO: On tap → show detail with metadata (size, format, label, sensor data)
    // TODO: Export FAB → onExportClick
}
