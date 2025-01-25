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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.background
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.layout.onSizeChanged



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
    var posX1 by remember { mutableStateOf(0f) } // Starting position in pixels
    var posY1 by remember { mutableStateOf(0f) }
    var size1 by remember { mutableStateOf(IntSize.Zero) }

    // State to track the current button state
    val density = LocalDensity.current // Access the current screen density

    var buttonState by remember { mutableStateOf<ButtonState>(ButtonState.Neutral) }


    Column(
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
                            Log.d("111","button1 ${offset.x} ${offset.y}")
                        }
                        ButtonState.Button2Clicked -> {

                        }
                        else -> {} // Do nothing in Neutral state
                    }
                    // Reset to Neutral state after the move
                    buttonState = ButtonState.Neutral
                    Log.d("nnn","buttonn ${offset.x} ${offset.y}")

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
                .absoluteOffset(x = with(density) { posX1.toDp() }, y = with(density) { posY1.toDp() }) // Use offset with Dp values
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
//                navController.popBackStack()  // Navigate back to home screen
                Log.d("Button1Pos","Button1 posx: $posX1 , posy: $posY1")
            },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(200.dp, 80.dp)
                .align(alignment = a) // Align the child to the bottom center
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