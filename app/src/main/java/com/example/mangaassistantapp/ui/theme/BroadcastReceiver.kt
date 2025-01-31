package com.example.mangaassistantapp.ui.theme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log

class VolumeUpReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == AudioManager.ACTION_VOLUME_UP) {
            // When the volume up button is pressed, start the service
            Log.d("VolumeUpReceiver", "Volume Up pressed")

            // Start the service to simulate a tap
            val serviceIntent = Intent(context, TapSimulationService::class.java)
            context.startService(serviceIntent)
        }
    }
}
