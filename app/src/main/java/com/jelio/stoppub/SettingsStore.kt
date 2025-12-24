package com.jelio.stoppub

import android.content.Context
import androidx.core.content.edit

object SettingsStore {

    private const val PREF_NAME = "app_settings"
    private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"

    fun isNotificationsEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true) // enabled by default
    }

    fun setNotificationsEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled) }
    }
}
