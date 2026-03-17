package com.example.datasetcam.sensor

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorMonitorScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val collector = remember { SensorCollector(context) }
    
    // Poll for updates (since we didn't implement Flows yet)
    var snapshot by remember { mutableStateOf(collector.getSnapshot()) }

    DisposableEffect(Unit) {
        collector.start()
        onDispose {
            collector.stop()
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            snapshot = collector.getSnapshot()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sensor Monitor") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SensorCard("Orientation", "Pitch: ${"%.1f".format(snapshot.pitch)}°\nRoll: ${"%.1f".format(snapshot.roll)}°")
            SensorCard("Environment", "Light: ${snapshot.lightLux} lux")
            SensorCard("Device Info", "Model: ${snapshot.device}\nStability: ${if (snapshot.gyroStable) "Steady" else "Moving"}")
            SensorCard("Last Updated", snapshot.timestamp)
        }
    }
}

@Composable
fun SensorCard(title: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
