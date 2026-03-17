package com.example.datasetcam.sensor

import androidx.compose.runtime.Composable

@Composable
fun SensorMonitorScreen(
    pitch: Float,
    roll: Float,
    lightLux: Float,
    isStable: Boolean,
    saveSensorData: Boolean,
    onToggleSave: (Boolean) -> Unit
) {
    // TODO: TopAppBar "Sensor Monitor"
    // TODO: Card rows showing Roll, Pitch, Light, Gyro status
    // TODO: Visual level indicator (animated tilt graphic)
    // TODO: Toggle switch "Save sensor data with images"
}
