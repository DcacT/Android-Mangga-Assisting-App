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
import androidx.compose.foundation.gestures.detectTapGestures
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
import android.util.Log


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
// Enum for Button States
enum class ButtonState {
    Neutral,
    Button1Clicked,
    Button2Clicked
}

@Composable
fun ConfigureScreen(navController: NavHostController) {
    // Variables to track the position of the two draggable buttons
    var posX1 by remember { mutableStateOf(50f) } // Starting position in pixels
    var posY1 by remember { mutableStateOf(50f) }
    var posX2 by remember { mutableStateOf(50f) }
    var posY2 by remember { mutableStateOf(100f) }
    // State to track the current button state

    var buttonState by remember { mutableStateOf<ButtonState>(ButtonState.Neutral) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                // Listen for a click anywhere on the screen
                detectTapGestures { offset ->
                    when (buttonState) {
                        ButtonState.Button1Clicked -> {
                            // Move Button 1 to the clicked location
                            posX1 = offset.x
                            posY1 = offset.y
                            Log.d("111","button1 ${offset.x} ${offset.y}")
                        }
                        ButtonState.Button2Clicked -> {
                            // Move Button 2 to the clicked location
                            posX2 = offset.x
                            posY2 = offset.y
                            Log.d("222","button2 ${offset.x} ${offset.y}")

                        }
                        else -> {} // Do nothing in Neutral state
                    }
                    // Reset to Neutral state after the move
                    buttonState = ButtonState.Neutral
                    Log.d("nnn","buttonn ${offset.x} ${offset.y}")

                }
            }

        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // First Draggable Button
        Button(
            onClick = {
                buttonState = ButtonState.Button1Clicked
                Log.d("Button1Clicked","Button1 posx: $posX1 , posy: $posY1")

            },
            modifier = Modifier
                .offset(x = posX1.dp, y = posY1.dp) // Use offset with Dp values
        ) {
            Text(text = "Volume Up")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Second Draggable Button
        Button(
            onClick = {
                buttonState = ButtonState.Button2Clicked
                Log.d("Button2Clicked","Button2 posx: $posX2 , posy: $posY2")

            },
            modifier = Modifier
                .offset(x = posX2.dp, y = posY2.dp) // Use offset with Dp values
        ) {
            Text(text = "Volume Down")
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