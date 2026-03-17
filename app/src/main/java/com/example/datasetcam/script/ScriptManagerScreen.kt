package com.example.datasetcam.script

import androidx.compose.runtime.Composable

@Composable
fun ScriptManagerScreen(
    scripts: List<ScriptModel>,
    activeScript: String?,
    onSelectScript: (ScriptModel) -> Unit,
    onImportScript: () -> Unit,
    onDeleteScript: (ScriptModel) -> Unit
) {
    // TODO: TopAppBar with title "Processing Scripts"
    // TODO: LazyColumn listing each script (name, checkmark if active)
    // TODO: On tap → onSelectScript to activate
    // TODO: Swipe-to-delete or long-press → onDeleteScript
    // TODO: FAB or button "Import Script" → onImportScript (file picker)
    // TODO: Below list: "Selected Script Preview" section showing pipeline steps
}
