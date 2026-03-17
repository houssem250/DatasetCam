package com.example.datasetcam.sensor

/**
 * Snapshot of sensor data at the moment of capture.
 */
data class SensorMetadata(
    val pitch: Float,       // degrees
    val roll: Float,        // degrees
    val lightLux: Float,    // ambient light
    val gyroStable: Boolean,// is the device steady?
    val device: String,     // Build.MODEL
    val timestamp: String   // ISO 8601
)
