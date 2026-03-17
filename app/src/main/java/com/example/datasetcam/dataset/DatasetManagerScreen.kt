package com.example.datasetcam.dataset

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatasetManagerScreen(
    images: List<DatasetImage>,
    labelCounts: Map<String, Int>,
    onDeleteImage: (DatasetImage) -> Unit,
    onExportClick: () -> Unit,
    onBack: () -> Unit
) {
    var selectedImage by remember { mutableStateOf<DatasetImage?>(null) }
    var filterLabel by remember { mutableStateOf<String?>(null) }

    val filteredImages = if (filterLabel == null) images else images.filter { it.label == filterLabel }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dataset Manager") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onExportClick) {
                        Icon(Icons.Default.FileDownload, contentDescription = "Export")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // Label Filter Chips
            ScrollableTabRow(
                selectedTabIndex = if (filterLabel == null) 0 else labelCounts.keys.indexOf(filterLabel) + 1,
                edgePadding = 16.dp,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                Tab(
                    selected = filterLabel == null,
                    onClick = { filterLabel = null },
                    text = { Text("All (${images.size})") }
                )
                labelCounts.forEach { (label, count) ->
                    Tab(
                        selected = filterLabel == label,
                        onClick = { filterLabel = label },
                        text = { Text("$label ($count)") }
                    )
                }
            }

            if (filteredImages.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No images found.")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredImages) { image ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { selectedImage = image }
                        ) {
                            AsyncImage(
                                model = File(image.filePath),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            
                            // Delete Overlay (Small icon top-right)
                            IconButton(
                                onClick = { onDeleteImage(image) },
                                modifier = Modifier.align(Alignment.TopEnd).size(32.dp).background(Color.Black.copy(alpha = 0.3f))
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Full Screen Detail Dialog
    if (selectedImage != null) {
        val image = selectedImage!!
        AlertDialog(
            onDismissRequest = { selectedImage = null },
            confirmButton = {
                TextButton(onClick = { selectedImage = null }) { Text("Close") }
            },
            title = { Text(image.fileName) },
            text = {
                Column {
                    AsyncImage(
                        model = File(image.filePath),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Label: ${image.label}", fontWeight = FontWeight.Bold)
                    Text("Dimensions: ${image.width} x ${image.height}")
                    Text("Size: ${image.sizeBytes / 1024} KB")
                    Text("Format: ${image.format}")
                    
                    if (!image.metadata.isNullOrEmpty()) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("Metadata:", style = MaterialTheme.typography.labelLarge)
                        image.metadata.forEach { (k, v) ->
                            Text("$k: $v", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        )
    }
}
