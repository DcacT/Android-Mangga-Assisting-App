package com.example.mangaassistantapp.ui.theme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.util.Log

class VolumeKeyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_MEDIA_BUTTON) {
            val keyEvent = intent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)

            if (keyEvent?.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                Log.d("VolumeKeyReceiver", "Volume Up Pressed")

                // Trigger the tap event by starting the AccessibilityService
                val serviceIntent = Intent(context, TapSimulationService::class.java)
                context.startService(serviceIntent)

                // Simulate tap at (100, 200)
                val service = TapSimulationService()
                service.simulateTap(100f, 200f)
            }
        }
    }
}
