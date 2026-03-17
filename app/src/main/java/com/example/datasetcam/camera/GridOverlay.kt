package com.example.datasetcam.camera

import androidx.compose.runtime.Composable

/**
 * Draws 3×3 grid lines + level indicator on top of camera preview.
 */
@Composable
fun GridOverlay(
    showGrid: Boolean,
    rollDegrees: Float,
    pitchDegrees: Float
) {
    // TODO: Canvas composable filling parent
    // TODO: if showGrid, draw 2 vertical + 2 horizontal lines (rule of thirds)
    // TODO: draw level indicator: short horizontal line that rotates with roll
    // TODO: change color green=level, yellow=slight tilt, red=too tilted
}
