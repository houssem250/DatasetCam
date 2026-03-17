package com.example.datasetcam.camera

import android.graphics.Bitmap
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.datasetcam.sensor.SensorCollector
import com.example.datasetcam.sensor.SensorMetadata
import androidx.compose.material.icons.filled.Sensors
import kotlinx.coroutines.delay
import android.widget.Toast

/**
 * Main camera Compose screen.
 */
@Composable
fun CameraScreen(
    onCapture: (Bitmap, SensorMetadata) -> Unit,
    onGalleryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSensorClick: () -> Unit,
    activeScriptName: String,
    sensorCollector: SensorCollector
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraManager = remember { CameraManager(context) }
    val previewView = remember { PreviewView(context) }

    // Real-time sensor values
    var roll by remember { mutableFloatStateOf(0f) }
    var pitch by remember { mutableFloatStateOf(0f) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(50) // 20fps update for UI
            roll = sensorCollector.roll
            pitch = sensorCollector.pitch
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // CameraX PreviewView
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize(),
            update = {
                cameraManager.startCamera(lifecycleOwner, it)
            }
        )

        // Grid & Level Overlay
        GridOverlay(
            showGrid = true,
            rollDegrees = roll,
            pitchDegrees = pitch
        )

        // Bottom Bar UI
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 48.dp)
        ) {
            // Script Name & Sensor Status (Bottom-Left)
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 24.dp)
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = CircleShape,
                    modifier = Modifier.clickable { onSensorClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Sensors,
                            contentDescription = null,
                            tint = if (sensorCollector.getSnapshot().gyroStable) Color.Green else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = activeScriptName,
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Capture Button (Center)
            IconButton(
                onClick = {
                    cameraManager.captureImage { bitmap ->
                        if (bitmap != null) {
                            val snapshot = sensorCollector.getSnapshot()
                            onCapture(bitmap, snapshot)
                            // Optional: Brief vibration or sound feedback could be added here
                        } else {
                            Toast.makeText(context, "Capture failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp)
                    .background(Color.White.copy(alpha = 0.3f), CircleShape)
                    .border(4.dp, Color.White, CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.White, CircleShape)
                )
            }

            // Controls (Bottom-Right)
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onGalleryClick) {
                    Icon(Icons.Default.Collections, contentDescription = "Gallery", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onSettingsClick) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraManager.shutdown()
        }
    }
}
