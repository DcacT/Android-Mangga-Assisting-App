package com.example.mangaassistantapp

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.util.Log          // For Log.d
import android.view.KeyEvent     // For KeyEvent\
val TAG = "MyAccessibilityService"

class MyAccessibilityService : AccessibilityService() {
    private var isRunning: Boolean = false

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
                    return true
                }
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    Log.d(TAG, "Vol Up")
                    return true
                }
            }
        }
        return false
    }
}