package com.jelio.stoppub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.jelio.stoppub.ui.theme.StopPubTheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StopPubTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CallLogsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CallLogsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val logs = remember { mutableStateListOf<CallLogEntry>() }
    var notificationsEnabled by remember { mutableStateOf(SettingsStore.isNotificationsEnabled(context)) }

    // fonction locale pour charger/rafraîchir les logs
    fun load() {
        logs.clear()
        try {
            val arr = CallLogStore.getLogs(context)
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                val number = obj.optString("number", "Inconnu")
                val blocked = obj.optBoolean("blocked", false)
                val date = obj.optLong("date", 0)
                logs.add(CallLogEntry(number, blocked, date))
            }
        } catch (_: Exception) {
            // en cas d'erreur JSON, on laisse la liste vide
        }
    }

    // Charger les logs au lancement
    LaunchedEffect(Unit) {
        load()
    }

    Column(modifier = modifier) {
        // en-tête avec bouton rafraîchir
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Journal d'appels", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.weight(1f))
            IconButton(onClick = { load() }) {
                // remplace l'icône material par un texte/emoji pour éviter la dépendance manquante
                Text(text = "⟳", fontSize = 18.sp)
            }
        }

        // Settings row for notification toggle
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Notifications d'appels bloqués", fontSize = 16.sp, modifier = Modifier.weight(1f))
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { enabled ->
                    notificationsEnabled = enabled
                    SettingsStore.setNotificationsEnabled(context, enabled)
                }
            )
        }

        if (logs.isEmpty()) {
            Text(text = "Aucun appel enregistré", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.then(Modifier)) {
                items(logs) { log ->
                    Row(modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                            Text(text = log.number, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = formatDate(log.date), fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (log.blocked) "Bloqué" else "Autorisé", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

fun formatDate(epochMillis: Long): String {
    if (epochMillis <= 0) return ""
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return try {
        sdf.format(Date(epochMillis))
    } catch (_: Exception) {
        ""
    }
}