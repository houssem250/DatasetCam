package com.example.datasetcam.script

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Overview: TopAppBar with title "Script Details"
/*
    Description:
        - TopAppBar with title "Script Details"
        - Back button to return to ScriptManagerScreen
        - LazyColumn listing each script (name, checkmark if active)
        - On tap → onSelectScript to activate
        - Swipe-to-delete or long-press → onDeleteScript
        - FAB or button "Import Script" → onImportScript (file picker)
        - Below list: "Selected Script Preview" section showing pipeline steps
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScriptDetailScreen(
    script: ScriptModel,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(script.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Pipeline Steps",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
            items(script.steps) { step ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = step.action.uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (step.params.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            step.params.forEach { (key, value) ->
                                Text(
                                    text = "$key: $value",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
