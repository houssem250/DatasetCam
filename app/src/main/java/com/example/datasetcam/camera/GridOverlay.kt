package com.example.datasetcam.camera

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.abs

/**
 * Draws 3×3 grid lines + level indicator on top of camera preview.
 */
@Composable
fun GridOverlay(
    showGrid: Boolean,
    rollDegrees: Float,
    pitchDegrees: Float
) {
    // Overview: Canvas composable filling parent
    // if showGrid, draw 2 vertical + 2 horizontal lines (rule of thirds)
    // draw level indicator: short horizontal line that rotates with roll
    // change color green=level, yellow=slight tilt, red=too tilted

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // 1. Draw Rule of Thirds Grid
        if (showGrid) {
            val gridColor = Color.White.copy(alpha = 0.4f)
            val strokeWidth = 1f
            
            // Vertical lines
            drawLine(gridColor, Offset(width / 3, 0f), Offset(width / 3, height), strokeWidth)
            drawLine(gridColor, Offset(2 * width / 3, 0f), Offset(2 * width / 3, height), strokeWidth)
            
            // Horizontal lines
            drawLine(gridColor, Offset(0f, height / 3), Offset(width, height / 3), strokeWidth)
            drawLine(gridColor, Offset(0f, 2 * height / 3), Offset(width, 2 * height / 3), strokeWidth)
        }

        // 2. Draw Level Indicator (Centered horizontal line that rotates)
        val isLevel = abs(rollDegrees) < 1.0f && abs(pitchDegrees) < 1.0f
        val indicatorColor = when {
            isLevel -> Color.Green
            abs(rollDegrees) < 5.0f -> Color.Yellow
            else -> Color.Red
        }

        val centerX = width / 2
        val centerY = height / 2
        val lineLength = 100f
        
        rotate(degrees = rollDegrees, pivot = Offset(centerX, centerY)) {
            drawLine(
                color = indicatorColor,
                start = Offset(centerX - lineLength, centerY),
                end = Offset(centerX + lineLength, centerY),
                strokeWidth = 4f
            )
            // Small notches at ends
            drawLine(indicatorColor, Offset(centerX - lineLength, centerY - 10), Offset(centerX - lineLength, centerY + 10), 2f)
            drawLine(indicatorColor, Offset(centerX + lineLength, centerY - 10), Offset(centerX + lineLength, centerY + 10), 2f)
        }
    }
}
