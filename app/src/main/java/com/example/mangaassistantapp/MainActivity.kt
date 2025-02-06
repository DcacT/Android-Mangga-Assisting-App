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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mangaassistantapp.ui.theme.MangaAssistantAppTheme
import android.util.Log
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.background
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.ButtonDefaults
import android.content.Intent
import com.example.mangaassistantapp.TapSimulationService

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
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("AppPreferences", android.content.Context.MODE_PRIVATE)
    val savedIsRunning = sharedPref.getBoolean("isRunning", false) // Provide a default value
    var isRunning by remember { mutableStateOf(savedIsRunning) }

    val serviceIntent = Intent(context, TapSimulationService::class.java)
    if (isRunning) {
        Log.d("isRunning", "Running")
        context.startService(serviceIntent) // Start the service
    } else {
        Log.d("isRunning", "Stopped")
        context.stopService(serviceIntent) // Stop the service
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = {navController.navigate("configure")},
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(200.dp, 80.dp)

        ){
            Text(text = "Configure")
        }
        Button(
            onClick = {
                isRunning = !isRunning
                val editor = sharedPref.edit()
                editor.putBoolean("isRunning", isRunning)
                editor.apply() // Apply changes asynchronously
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = if (isRunning) Color.Green else Color.Red // Change color based on state
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(200.dp, 80.dp)

        ){
            Text(text = if (isRunning) "Running" else "Paused")
        }
    }
}
// Enum for Button States
enum class ButtonState {
    Neutral,
    Button1Clicked,
    Button2Clicked
}

@Composable
fun ConfigureScreen(navController: NavHostController) {

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("AppPreferences", android.content.Context.MODE_PRIVATE)
    val savedPosX1 = sharedPref.getFloat("posX1", 100f) // Provide a default value
    val savedPosY1 = sharedPref.getFloat("posY1", 200f) // Provide a default value

    var posX1 by remember { mutableStateOf(savedPosX1) }
    var posY1 by remember { mutableStateOf(savedPosY1) }


    var size1 by remember { mutableStateOf(IntSize.Zero) }

    // State to track the current button state
    val density = LocalDensity.current // Access the current screen density

    var buttonState by remember { mutableStateOf<ButtonState>(ButtonState.Neutral) }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue) // Set the background color to blue

//            .padding(16.dp)
            .pointerInput(Unit) {
                // Listen for a click anywhere on the screen
                detectTapGestures { offset ->
                    when (buttonState) {
                        ButtonState.Button1Clicked -> {
                            // Move Button 1 to the clicked location
                            posX1 = offset.x - (size1.width / 2)
                            posY1 = offset.y - (size1.height / 2)

                            sharedPref.edit().putFloat("posX1", posX1).apply()
                            sharedPref.edit().putFloat("posY1", posY1).apply()

                            Log.d("111", "button1 ${offset.x} ${offset.y}")
                        }

                        ButtonState.Button2Clicked -> {

                        }

                        else -> {} // Do nothing in Neutral state
                    }
                    // Reset to Neutral state after the move
                    buttonState = ButtonState.Neutral
                    Log.d("nnn", "buttonn ${offset.x} ${offset.y}")

                }
            }

        ,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {
        // First Draggable Button
        Button(
            onClick = {
                buttonState = ButtonState.Button1Clicked
                Log.d("Button1Clicked","Button1 posx: $posX1 , posy: $posY1")

            },
            modifier = Modifier
                .absoluteOffset(
                    x = with(density) { posX1.toDp() },
                    y = with(density) { posY1.toDp() }) // Use offset with Dp values
                .onSizeChanged { newSize ->
                    size1 = newSize // Save the width and height
                }
        ) {
            Text(text = "Volume Up")
        }



        // Save & Exit Button
        Button(
            onClick = {
                // Save logic can go here (if needed)
                val allEntries = sharedPref.all // Get all key-value pairs as a Map
                for ((key, value) in allEntries) {
                    Log.d("SharedPreferences", "Key: $key, Value: $value")
                }

                Log.d("savedPosX1", "Value: $savedPosX1")
                Log.d("savedPosY1", "Value: $savedPosY1")

                navController.navigate("home")            },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(200.dp, 80.dp)
                .align(alignment = Alignment.BottomCenter) // Align the child to the bottom center
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