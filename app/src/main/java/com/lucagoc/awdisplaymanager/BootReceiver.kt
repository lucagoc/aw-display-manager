package com.lucagoc.awdisplaymanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val prefs = context.getSharedPreferences("display_settings", Context.MODE_PRIVATE)

                // 1. Restore output resolution
                val savedModeId = prefs.getInt("saved_output_mode_id", -1)
                val savedModeName = prefs.getString("saved_output_mode_name", "") ?: ""
                if (savedModeId != -1) {
                    DisplayManager.setResolution(savedModeId)
                    DisplayManager.saveOutputResolutionPersist(savedModeId)
                }

                // 2. Restore or compute render resolution
                val isAutoRender = prefs.getBoolean("pref_auto_render_resolution", true)
                val renderRes = if (isAutoRender) {
                    val currentOutput = if (savedModeName.isNotEmpty()) savedModeName else DisplayManager.getCurrentResolution()
                    DisplayManager.computeAutoRenderResolution(currentOutput)
                } else {
                    prefs.getString("pref_manual_render_resolution", "1920x1080") ?: "1920x1080"
                }
                DisplayManager.setRenderResolution(renderRes)

                // 3. Restore or compute density
                val isAutoDensity = prefs.getBoolean("pref_auto_density", true)
                val density = if (isAutoDensity) {
                    DisplayManager.computeRecommendedDpi(renderRes)
                } else {
                    prefs.getInt("pref_manual_density", 320)
                }
                DisplayManager.setDensity(density)

                // 4. Restore overscan if set
                val savedMargin = prefs.getInt("saved_overscan_margin", -1)
                if (savedMargin in 80..100) {
                    DisplayManager.setOverscan(savedMargin)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pendingResult.finish()
            }
        }
    }
}
