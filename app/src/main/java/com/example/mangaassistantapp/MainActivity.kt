package com.example.mangaassistantapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.ui.unit.dp

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mangaassistantapp.ui.theme.MangaAssistantAppTheme
import androidx.compose.ui.input.pointer.pointerInput

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MangaAssistantAppTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController) }
                    composable("configure") { ConfigureScreen(navController) }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = "Hello $name!"
    )
}

@Composable
fun HomeScreen(navController: NavHostController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = {navController.navigate("configure")},
            modifier = Modifier.padding(bottom = 16.dp).size(200.dp, 80.dp)

        ){
            Text(text = "Configure")
        }
        Button(
            onClick = {},
            modifier = Modifier.padding(bottom = 16.dp).size(200.dp, 80.dp)
        ){
            Text(text = "Start")
        }
    }
}
@Composable
fun ConfigureScreen(navController: NavHostController) {
    // Variables to track the position of the two draggable buttons
    var posX1 by remember { mutableStateOf(200f) } // Starting position in pixels
    var posY1 by remember { mutableStateOf(200f) }
    var posX2 by remember { mutableStateOf(400f) }
    var posY2 by remember { mutableStateOf(400f) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // First Draggable Button
        Button(
            onClick = {},
            modifier = Modifier
                .offset(x = posX1.dp, y = posY1.dp) // Use offset with Dp values
                .pointerInput(Unit) {
                    detectDragGestures { _, pan, ->
                        posX1 += pan.x // Update position with absolute pixel values
                        posY1 += pan.y
                    }
                }
        ) {
            Text(text = "Drag Me 1")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Second Draggable Button
        Button(
            onClick = {},
            modifier = Modifier
                .offset(x = posX2.dp, y = posY2.dp) // Use offset with Dp values
                .pointerInput(Unit) {
                    detectDragGestures { _, pan ->
                        posX1 += pan.x // Update position with absolute pixel values
                        posY1 += pan.y
                    }
                }
        ) {
            Text(text = "Drag Me 2")
        }

        Spacer(modifier = Modifier.weight(1f)) // Push the Save & Exit button to the bottom

        // Save & Exit Button
        Button(
            onClick = {
                // Save logic can go here (if needed)
                navController.popBackStack()  // Navigate back to home screen
            },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(200.dp, 80.dp)
        ) {
            Text(text = "Save & Exit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MangaAssistantAppTheme {
        Greeting("Android")
    }
}