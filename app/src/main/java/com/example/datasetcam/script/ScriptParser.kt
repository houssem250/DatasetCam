package com.example.datasetcam.script

import com.example.datasetcam.pipeline.ImageOperation
import com.example.datasetcam.pipeline.OperationRegistry
import org.json.JSONArray
import org.json.JSONObject

/**
 * Parses JSON script files into ScriptModel and executable operations.
 */
class ScriptParser {

    /** Parse JSON string → ScriptModel */
    // Overview: use JSONArray to parse the top-level array
    // Overview: for each JSONObject, extract "action" + remaining keys as params
    // Overview: return ScriptModel with name derived from fileName 
    fun parse(json: String, fileName: String): ScriptModel {
        val steps = mutableListOf<ScriptStep>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val action = obj.getString("action")
                
                // Extract remaining keys as params
                val params = mutableMapOf<String, Any>()
                val keys = obj.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    if (key != "action") {
                        params[key] = obj.get(key)
                    }
                }
                steps.add(ScriptStep(action, params))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val name = fileName.substringBeforeLast(".")
        return ScriptModel(name, fileName, steps)
    }

    /** Convert ScriptModel → List<ImageOperation> using OperationRegistry */
    fun toOperations(script: ScriptModel): List<ImageOperation> {
        // Overview: iterate through script.steps
        // Overview: for each step, call OperationRegistry.create(step.action, step.params)
        // Overview: collect results into a List<ImageOperation>
        // Overview: return the list
        return script.steps.mapNotNull { step ->
            try {
                OperationRegistry.create(step.action, step.params)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
