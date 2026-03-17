package com.example.datasetcam.navigation

import com.example.datasetcam.navigation.AppNavigation
import com.example.datasetcam.sensor.SensorCollector
import com.example.datasetcam.sensor.SensorMonitorScreen

/**
 * Top-level navigation using Jetpack Compose Navigation.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    
    // Repositories & Services
    val scriptRepo = remember { ScriptRepository(context) }
    val datasetRepo = remember { DatasetRepository(context) }
    val sensorCollector = remember { SensorCollector(context) }
    
    // Lifecycle-aware sensor management
    DisposableEffect(Unit) {
        sensorCollector.start()
        onDispose {
            sensorCollector.stop()
        }
    }

    // Ensure default script exists
    LaunchedEffect(Unit) {
        scriptRepo.ensureDefaultScript()
    }

    NavHost(navController = navController, startDestination = "camera") {
        composable("camera") {
            val activeScriptName = scriptRepo.getActiveScript() ?: "default_processing"
            val scope = rememberCoroutineScope()
            
            CameraScreen(
                onCapture = { bitmap, snapshot ->
                    // 1. Get script operations
                    val scriptJson = scriptRepo.getScriptContent(activeScriptName)
                    if (scriptJson != null) {
                        val scriptModel = com.example.datasetcam.script.ScriptParser.parse(scriptJson)
                        val operations = com.example.datasetcam.script.ScriptParser.toOperations(scriptModel)
                        
                        // 2. Wrap sensor data as metadata map
                        val metadata = mapOf(
                            "pitch" to snapshot.pitch,
                            "roll" to snapshot.roll,
                            "light" to snapshot.lightLux,
                            "device" to snapshot.device,
                            "timestamp" to snapshot.timestamp
                        )
                        
                        // 3. Execute pipeline in background
                        val executor = com.example.datasetcam.pipeline.PipelineExecutor()
                        executor.execute(bitmap, operations, metadata)
                        // Note: SaveOperation (if in script) will handle the writing
                    }
                },
                onGalleryClick = { navController.navigate("dataset") },
                onSettingsClick = { navController.navigate("scripts") },
                onSensorClick = { navController.navigate("sensor") },
                activeScriptName = activeScriptName,
                sensorCollector = sensorCollector
            )
        }

        composable("scripts") {
            val scripts = remember { scriptRepo.listScripts() }
            val activeScript = scriptRepo.getActiveScript()
            ScriptManagerScreen(
                scripts = scripts,
                activeScriptName = activeScript,
                onSelectScript = { script ->
                    scriptRepo.setActiveScript(script.name)
                    navController.popBackStack()
                },
                onImportScript = { /* TODO: File picker */ },
                onDeleteScript = { script -> scriptRepo.deleteScript(script.name) },
                onBack = { navController.popBackStack() }
            )
        }

        composable("dataset") {
            val images = datasetRepo.listImages()
            val labelCounts = datasetRepo.getLabelCounts()
            DatasetManagerScreen(
                images = images,
                labelCounts = labelCounts,
                onDeleteImage = { img -> datasetRepo.deleteImage(img) },
                onExportClick = { /* TODO: Export dialog */ },
                onBack = { navController.popBackStack() }
            )
        }

        composable("sensor") {
            SensorMonitorScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
