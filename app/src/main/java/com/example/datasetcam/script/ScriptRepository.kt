package com.example.datasetcam.script

import android.content.Context
import android.net.Uri

/**
 * Manages script files on device storage.
 */
class ScriptRepository(private val context: Context) {

    /** List all .json script files in the scripts directory */
    fun listScripts(): List<ScriptModel> {
        // TODO: get scripts directory from StorageManager
        // TODO: list .json files, parse each with ScriptParser
        // TODO: return list of ScriptModels
        return emptyList()
    }

    /** Import a script from external URI (file picker) */
    fun importScript(uri: Uri): ScriptModel? {
        // TODO: read file content from URI via ContentResolver
        // TODO: copy to internal scripts directory
        // TODO: parse and return ScriptModel
        return null
    }

    /** Delete a script by name */
    fun deleteScript(name: String): Boolean {
        // TODO: find file, delete, return success
        return false
    }

    /** Get the currently active script name from SharedPreferences */
    fun getActiveScript(): String? {
        // TODO: read from SharedPreferences
        return null
    }

    /** Set the active script */
    fun setActiveScript(name: String) {
        // TODO: write to SharedPreferences
    }
}
