package com.example.datasetcam.sensor

import android.content.Context

/**
 * Reads accelerometer, gyroscope, light sensors in real-time.
 */
class SensorCollector(private val context: Context) {

    /** Start listening to sensors */
    fun start() {
        // TODO: get SensorManager system service
        // TODO: register listeners for TYPE_ACCELEROMETER, TYPE_GYROSCOPE, TYPE_LIGHT
        // TODO: compute pitch/roll from accelerometer using atan2
        // TODO: determine gyroStable from gyroscope magnitude threshold
    }

    /** Stop listening */
    fun stop() {
        // TODO: unregister all sensor listeners
    }

    /** Get current sensor snapshot */
    fun capture(): SensorMetadata? {
        // TODO: return SensorMetadata with latest values + Build.MODEL + ISO timestamp
        return null
    }

    // TODO: add Flow / LiveData properties to publish live values to UI
}
