package com.jelio.stoppub

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import androidx.core.content.edit

object CallLogStore {

    private const val PREF_NAME = "call_logs"
    private const val KEY_LOGS = "logs"
    private const val MAX_LOGS = 50

    fun addLog(context: Context, number: String?, blocked: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val oldArray = JSONArray(prefs.getString(KEY_LOGS, "[]"))

        val log = JSONObject().apply {
            put("number", number ?: "Inconnu")
            put("blocked", blocked)
            put("date", System.currentTimeMillis())
        }

        val newArray = JSONArray().apply {
            put(log)
            for (i in 0 until oldArray.length()) {
                put(oldArray.get(i))
            }
        }

        while (newArray.length() > MAX_LOGS) {
            newArray.remove(newArray.length() - 1)
        }

        prefs.edit { putString(KEY_LOGS, newArray.toString()) }
    }

    fun getLogs(context: Context): JSONArray {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return JSONArray(prefs.getString(KEY_LOGS, "[]"))
    }
}
