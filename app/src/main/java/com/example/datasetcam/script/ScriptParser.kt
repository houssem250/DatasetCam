package com.example.datasetcam.script

import com.example.datasetcam.pipeline.ImageOperation
import com.example.datasetcam.pipeline.OperationRegistry

/**
 * Parses JSON script files into ScriptModel and executable operations.
 */
class ScriptParser {

    /** Parse JSON string → ScriptModel */
    fun parse(json: String, fileName: String): ScriptModel {
        // TODO: use JSONArray to parse the top-level array
        // TODO: for each JSONObject, extract "action" + remaining keys as params
        // TODO: return ScriptModel with name derived from fileName
        return ScriptModel("", "", emptyList())
    }

    /** Convert ScriptModel → List<ImageOperation> using OperationRegistry */
    fun toOperations(script: ScriptModel): List<ImageOperation> {
        // TODO: for each step, call OperationRegistry.create(step.action, step.params)
        // TODO: return list of operations
        return emptyList()
    }
}
