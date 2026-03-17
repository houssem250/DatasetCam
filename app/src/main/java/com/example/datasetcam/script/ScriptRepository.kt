package com.example.datasetcam.script

import android.content.Context
import android.net.Uri
import com.example.datasetcam.storage.StorageManager
import java.io.File

/**
 * Manages script files on device storage.
 */
class ScriptRepository(private val context: Context) {

    private val parser = ScriptParser()
    private val prefs = context.getSharedPreferences("datasetcam_prefs", Context.MODE_PRIVATE)

    /** List all .json script files in the scripts directory */
    fun listScripts(): List<ScriptModel> {
        // Overview: get scripts directory from StorageManager
        // Overview: list all .json files in the directory
        // Overview: parse each file using ScriptParser.parse()
        // Overview: return list of ScriptModel
        val dir = StorageManager.getScriptsDir(context)
        val files = dir.listFiles { _, name -> name.endsWith(".json") } ?: return emptyList()
        
        return files.map { file ->
            val json = file.readText()
            parser.parse(json, file.name)
        }
    }

    /** Import a script from external URI (file picker) */
    fun importScript(uri: Uri): ScriptModel? {
        // Overview: open input stream from URI
        // Overview: read the JSON content
        // Overview: get file name from URI
        // Overview: save to scripts directory
        // Overview: parse and return ScriptModel
        return try {
            val content = context.contentResolver.openInputStream(uri)?.use { it.bufferedReader().readText() } ?: return null
            val fileName = getFileNameFromUri(uri) ?: "imported_script_${System.currentTimeMillis()}.json"
            
            val destFile = File(StorageManager.getScriptsDir(context), fileName)
            destFile.writeText(content)
            
            parser.parse(content, fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        return uri.path?.substringAfterLast("/")
    }

    /** Delete a script by name */
    fun deleteScript(name: String): Boolean {
        // Overview: ensure name has .json suffix
        // Overview: get file from scripts directory
        // Overview: delete if exists
        // Overview: return true if deleted, false otherwise
        val fileName = if (name.endsWith(".json")) name else "$name.json"
        val file = File(StorageManager.getScriptsDir(context), fileName)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }

    /** Get the currently active script name from SharedPreferences */
    fun getActiveScript(): String? {
        return prefs.getString("active_script", null)
    }

    /** Set the active script */
    fun setActiveScript(name: String) {
        prefs.edit().putString("active_script", name).apply()
    }

    /** Create a default script if none exist */
    fun ensureDefaultScript() {
        val dir = StorageManager.getScriptsDir(context)
        if (dir.listFiles()?.isEmpty() == true) {
            val defaultJson = """
                [
                  {"action": "resize", "width": 224, "height": 224},
                  {"action": "save", "format": "png", "label": "sample"}
                ]
            """.trimIndent()
            File(dir, "default_processing.json").writeText(defaultJson)
            setActiveScript("default_processing")
        }
    }
}
