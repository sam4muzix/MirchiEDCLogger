package com.mirchi.edclogger.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mirchi.edclogger.utils.GoogleSheetLogger
import com.mirchi.edclogger.utils.getCurrentRJName

@Composable
fun AutomationScreen(navController: NavHostController) {
    val context = LocalContext.current
    var syncEnabled by remember { mutableStateOf(false) }
    var reportEnabled by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(false) }
    val currentRJ = getCurrentRJName()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Current RJ: $currentRJ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Auto Sync")
            Spacer(Modifier.weight(1f))
            Switch(
                checked = syncEnabled,
                onCheckedChange = {
                    syncEnabled = it
                    GoogleSheetLogger.logToSheet(
                        app = "System",
                        rj = currentRJ,
                        contact = "Auto",
                        messageOrCall = if (it) "Sync Enabled" else "Sync Disabled",
                        gender = "",
                        location = "",
                        age = "",
                        isBeyondTime = false
                    )
                    Toast.makeText(context, "Sync ${if (it) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
                }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Daily Reports")
            Spacer(Modifier.weight(1f))
            Switch(
                checked = reportEnabled,
                onCheckedChange = {
                    reportEnabled = it
                    GoogleSheetLogger.logToSheet(
                        app = "System",
                        rj = currentRJ,
                        contact = "Auto",
                        messageOrCall = if (it) "Daily Report Enabled" else "Daily Report Disabled",
                        gender = "",
                        location = "",
                        age = "",
                        isBeyondTime = false
                    )
                    Toast.makeText(context, "Report ${if (it) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
                }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Notifications")
            Spacer(Modifier.weight(1f))
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = {
                    notificationsEnabled = it
                    GoogleSheetLogger.logToSheet(
                        app = "System",
                        rj = currentRJ,
                        contact = "Auto",
                        messageOrCall = if (it) "Notifications Enabled" else "Notifications Disabled",
                        gender = "",
                        location = "",
                        age = "",
                        isBeyondTime = false
                    )
                    Toast.makeText(context, "Notifications ${if (it) "Enabled" else "Disabled"}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
