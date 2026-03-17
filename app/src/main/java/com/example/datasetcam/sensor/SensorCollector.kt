package com.example.datasetcam.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import java.time.Instant
import java.time.format.DateTimeFormatter
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * Manages real-time sensor readings (accelerometer, magnet, light).
 */
class SensorCollector(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    
    // Sensor data
    private var gravity = FloatArray(3)
    private var geomagnetic = FloatArray(3)
    private var lightValue = 0f
    
    // Orientation
    var pitch = 0f
    var roll = 0f
    
    fun start() {
        val accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        
        accel?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        magnet?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        light?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> gravity = event.values.clone()
            Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = event.values.clone()
            Sensor.TYPE_LIGHT -> lightValue = event.values[0]
        }
        
        calculateOrientation()
    }

    private fun calculateOrientation() {
        val r = FloatArray(9)
        val i = FloatArray(9)
        if (SensorManager.getRotationMatrix(r, i, gravity, geomagnetic)) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(r, orientation)
            
            // Convert to degrees
            // pitch: rotation around X axis (-180 to 180)
            // roll: rotation around Y axis (-90 to 90)
            pitch = Math.toDegrees(orientation[1].toDouble()).toFloat()
            roll = Math.toDegrees(orientation[2].toDouble()).toFloat()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    /** Capture a snapshot of current sensor states */
    fun getSnapshot(): SensorMetadata {
        return SensorMetadata(
            pitch = pitch,
            roll = roll,
            lightLux = lightValue,
            gyroStable = isSteady(),
            device = Build.MODEL,
            timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        )
    }

    private fun isSteady(): Boolean {
        // Simple heuristic: if gravity vector is close to 9.8 and not changing much
        val g = sqrt(gravity[0]*gravity[0] + gravity[1]*gravity[1] + gravity[2]*gravity[2])
        return g > 9.0 && g < 10.5
    }
}
