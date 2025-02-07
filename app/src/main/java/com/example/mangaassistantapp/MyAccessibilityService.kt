package com.example.mangaassistantapp

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.util.Log          // For Log.d
import android.view.KeyEvent     // For KeyEvent\
import android.content.SharedPreferences
import android.content.Context
val TAG = "MyAccessibilityService"

class MyAccessibilityService : AccessibilityService() {
    private var isRunning: Boolean = false
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate() {
        sharedPref = applicationContext.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        super.onCreate()
    }
    private fun isServiceEnabled(): Boolean {
        Log.d(TAG, "Check Service Connection")
        try {
            val enabledServices = Settings.Secure.getString(
                contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            Log.d("MyAccessibilityService", "Package Name || ${packageName}/${MyAccessibilityService::class.java.name}")

            return enabledServices.contains("${packageName}/${MyAccessibilityService::class.java.name}")
        } catch (e: Exception) {
            Log.e("MyAccessibilityService", "Error checking if service is enabled: ${e.message}")
            return false
        }
    }

    private fun redirectToAccessibilitySettings() {
        Log.d(TAG, "Redirecting To Settings")
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    fun simulateTap(x: Float, y: Float) {
        val path = Path().apply {
            moveTo(x, y) // Move to tap position (100, 200)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 100))
            .build()

        val dispatched = dispatchGesture(gesture, null, null)
        Log.d("AutoClicker", "Tap at ($x, $y) dispatched: $dispatched")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        val isRunningValue = intent?.getStringExtra("isRunning") // Retrieve data
        Log.d(TAG, "onStartCommand isRunning $isRunningValue")
        Log.d(TAG, "onStartCommand isRunning ${isRunningValue == "true"}")

        if(isRunningValue == null){
            redirectToAccessibilitySettings()
        }else{
            isRunning = (isRunningValue == "true")
        }


        // Continue with the usual service start logic
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onServiceConnected() {

        super.onServiceConnected()
        Log.d(TAG, "service is connected")
        stopSelf()
    }


    override fun onDestroy() {
        Log.d(TAG, "Service has been destroyed.")

        super.onDestroy()
        // Stop the service if it's not already stopped
    }
    override fun onAccessibilityEvent(accessibilityEvent: AccessibilityEvent) {
        Log.d(TAG, "onAccessibilityEvent$accessibilityEvent")
    }

    override fun onInterrupt() {
        Log.d(TAG, "Service is Interrupted")
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        Log.d(TAG, "isRunnign $isRunning")

        return if (isRunning) {
            // Handle the key event if the service is running
            handleKeyEvent(event)
        } else {
            // If the service is not running, either call super.onKeyEvent or return false
            super.onKeyEvent(event)
            // You can also return false if you don't want to handle the event when it's not running
        }
    }


    private fun handleKeyEvent(event: KeyEvent): Boolean {
        val action: Int = event.getAction()
        val keyCode: Int = event.getKeyCode()
        if (!isRunning){
            return false
        }
        if (action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_VOLUME_DOWN ->{
                    Log.d(TAG, "Vol Down")
                    val volDn_x = sharedPref.getFloat("volDn_x", 300f) // Provide a default value
                    val volDn_y = sharedPref.getFloat("volDn_y", 300f) // Provide a default value
                    simulateTap(volDn_x,volDn_y)
                    return true
                }
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    Log.d(TAG, "Vol Up")

                    val volUp_x = sharedPref.getFloat("volUp_x", 0f) // Provide a default value
                    val volUp_y = sharedPref.getFloat("volUp_y", 0f) // Provide a default value
                    simulateTap(volUp_x,volUp_y)
                    return true
                }
            }
        }
        return false
    }
}