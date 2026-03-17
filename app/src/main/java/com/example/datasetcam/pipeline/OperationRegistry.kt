package com.example.datasetcam.pipeline

import android.content.Context
import com.example.datasetcam.pipeline.operations.*

/**
 * Maps JSON action strings (e.g. "resize", "grayscale") to operation factories.
 * Extensible: users can register new operations.
 */
object OperationRegistry {

    private val factories = mutableMapOf<String, (Map<String, Any>) -> ImageOperation>()

    /** Register a factory for an action name */
    fun register(action: String, factory: (Map<String, Any>) -> ImageOperation) {
        factories[action] = factory
    }

    /** Create an operation from action name + params */
    fun create(action: String, params: Map<String, Any>): ImageOperation {
        val factory = factories[action] ?: throw IllegalArgumentException("Unknown operation: $action")
        return factory(params)
    }

    /** Register all built-in operations (resize, grayscale, etc.) */
    fun registerDefaults(context: Context, outputDir: String) {
        // Overview: register all built-in operations (resize, grayscale, etc.)
        
        register("resize") { params ->
            val w = (params["width"] as? Number)?.toInt() ?: 224
            val h = (params["height"] as? Number)?.toInt() ?: 224
            ResizeOperation(w, h)
        }
        
        register("grayscale") { _ -> GrayscaleOperation() }
        
        register("normalize") { _ -> NormalizeOperation() }
        
        register("rotate") { params ->
            val deg = (params["degrees"] as? Number)?.toFloat() ?: 0f
            RotateOperation(deg)
        }
        
        register("crop") { params ->
            val x = (params["x"] as? Number)?.toInt() ?: 0
            val y = (params["y"] as? Number)?.toInt() ?: 0
            val w = (params["width"] as? Number)?.toInt() ?: 100
            val h = (params["height"] as? Number)?.toInt() ?: 100
            CropOperation(x, y, w, h)
        }
        
        register("blur") { params ->
            val r = (params["radius"] as? Number)?.toFloat() ?: 5f
            BlurOperation(context, r)
        }
        
        register("edge_detection") { _ -> EdgeDetectionOperation() }
        
        register("save") { params ->
            val format = params["format"] as? String ?: "png"
            val label = params["label"] as? String ?: "unlabeled"
            SaveOperation(format, outputDir, label)
        }
    }
}
