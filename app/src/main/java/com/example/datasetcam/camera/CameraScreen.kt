package com.example.datasetcam.camera

import androidx.compose.runtime.Composable

/**
 * Main camera Compose screen.
 */
@Composable
fun CameraScreen(
    onCapture: () -> Unit,
    onGalleryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    activeScriptName: String
) {
    // TODO: CameraX PreviewView via AndroidView composable
    // TODO: GridOverlay composable on top of preview
    // TODO: Text showing active script name at bottom-left
    // TODO: Bottom bar with Gallery, Capture (large circle), Settings buttons
    // TODO: Capture button triggers onCapture callback
}
