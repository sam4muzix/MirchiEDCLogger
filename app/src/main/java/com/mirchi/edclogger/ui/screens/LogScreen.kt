package com.mirchi.edclogger.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LogsScreen(logs: List<LogEntry>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Today's Logs",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (logs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No logs found", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(logs) { log ->
                    LogCard(log)
                }
            }
        }
    }
}

@Composable
fun LogCard(log: LogEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("${log.time} — ${log.app}", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text("Contact: ${log.contact}", fontSize = 14.sp)
            Text("Message: ${log.messageOrCall}", fontSize = 14.sp)
            Text("Location: ${log.location} | Gender: ${log.gender} | Age: ${log.age}", fontSize = 12.sp)
        }
    }
}

// You can update this model or fetch it from ViewModel/database
data class LogEntry(
    val time: String,
    val app: String,
    val contact: String,
    val messageOrCall: String,
    val gender: String,
    val location: String,
    val age: String
)
