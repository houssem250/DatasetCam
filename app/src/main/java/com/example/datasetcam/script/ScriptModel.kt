package com.example.datasetcam.script

/**
 * Represents a processing script loaded from JSON.
 */
data class ScriptModel(
    val name: String,           // e.g. "resize128_gray"
    val fileName: String,       // e.g. "resize128_gray.json"
    val steps: List<ScriptStep> // ordered list of actions
)

data class ScriptStep(
    val action: String,                // e.g. "resize", "grayscale"
    val params: Map<String, Any>       // e.g. {"width": 128, "height": 128}
)
