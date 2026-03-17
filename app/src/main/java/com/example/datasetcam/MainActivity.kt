package com.example.datasetcam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.datasetcam.ui.theme.DatasetCamTheme
import com.example.datasetcam.pipeline.OperationRegistry
import com.example.datasetcam.storage.StorageManager
// import com.example.datasetcam.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Overview: request camera + sensor permissions
        // Overview: initialize OperationRegistry.registerDefaults()

        // Initialize the image processing pipeline registry
        val datasetDir = StorageManager.getDatasetDir(this).absolutePath
        OperationRegistry.registerDefaults(this, datasetDir)
        
        enableEdgeToEdge()
        setContent {
            DatasetCamTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // TODO: Replace Greeting with AppNavigation()
                    Greeting(
                        name = "DatasetCam Developer",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!\n\nThe skeleton structure is ready.",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DatasetCamTheme {
        Greeting("Android")
    }
}
/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DatasetCamTheme {
        Greeting("Android")
    }
}*/