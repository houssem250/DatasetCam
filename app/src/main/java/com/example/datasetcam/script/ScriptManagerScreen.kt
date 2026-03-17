package com.example.datasetcam.script

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScriptManagerScreen(
    scripts: List<ScriptModel>,
    // OLD: activeScript: String?,
    activeScriptName: String?,
    onSelectScript: (ScriptModel) -> Unit,
    onImportScript: () -> Unit,
    // OVERVIEW: TopAppBar with title "Processing Scripts"
    // OVERVIEW: LazyColumn listing each script (name, checkmark if active)
    // OVERVIEW: On tap → onSelectScript to activate
    // OVERVIEW: Swipe-to-delete or long-press → onDeleteScript
    // OVERVIEW: FAB or button "Import Script" → onImportScript (file picker)
    // OVERVIEW: Below list: "Selected Script Preview" section showing pipeline steps
    
    onDeleteScript: (ScriptModel) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Processing Scripts") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onImportScript,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Import Script") }
            )
        }
    ) { innerPadding ->
        if (scripts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No scripts found. Import a .json script to begin.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(scripts) { script ->
                    val isActive = script.name == activeScriptName
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectScript(script) },
                        colors = if (isActive) {
                            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                        } else {
                            CardDefaults.cardColors()
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = script.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    text = "${script.steps.size} steps",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            
                            Row {
                                if (isActive) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = "Active",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                }
                                
                                IconButton(onClick = { onDeleteScript(script) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                                }
                            }
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
                }
            }
        }
    }
}
