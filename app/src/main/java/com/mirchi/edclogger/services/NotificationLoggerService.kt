package com.mirchi.edclogger.services

import android.provider.CallLog
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.mirchi.edclogger.utils.GoogleSheetLogger
import com.mirchi.edclogger.utils.getCurrentRJName
import java.text.SimpleDateFormat
import java.util.*

class NotificationLoggerService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val extras = sbn.notification.extras
        val title = extras.getString("android.title") ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        val appType = when {
            packageName.contains("whatsapp", ignoreCase = true) -> "WhatsApp"
            packageName.contains("dialer", ignoreCase = true) || packageName.contains("phone", ignoreCase = true) -> "Call"
            else -> return
        }

        val junkPatterns = listOf(
            Regex("^[0-9]+ messages? from", RegexOption.IGNORE_CASE),
            Regex("messages? from [0-9]+ chats", RegexOption.IGNORE_CASE),
            Regex("checking for new messages", RegexOption.IGNORE_CASE),
            Regex("^\\d+ new messages$", RegexOption.IGNORE_CASE),
        )
        if (text.isBlank() || junkPatterns.any { it.containsMatchIn(text) }) return

        if (appType == "WhatsApp" && text.isBlank()) return

        val now = Calendar.getInstance()
        val hour = now.get(Calendar.HOUR_OF_DAY)
        val minute = now.get(Calendar.MINUTE)
        val isBeyondTime = hour < 7 || (hour == 21 && minute > 0) || hour > 21
        val rjName = if (isBeyondTime) "BeyondTime" else getCurrentRJName()

        val contact = when (appType) {
            "Call" -> getLastCallDetails().first
            "WhatsApp" -> title
            else -> "Unknown"
        }

        val messageOrCall = when (appType) {
            "Call" -> getLastCallDetails().second
            "WhatsApp" -> text
            else -> "Call"
        }

        val gender = "Unspecified"
        val location = "Unknown"
        val age = "N/A"

        Log.d("NotifierService", "[Log] App: $appType, Contact: $contact, Message/Call: $messageOrCall")

        GoogleSheetLogger.logToSheet(
            app = appType,
            rj = rjName,
            contact = contact,
            messageOrCall = messageOrCall,
            gender = gender,
            location = location,
            age = age,
            isBeyondTime = isBeyondTime
        )
    }

    private fun getLastCallDetails(): Pair<String, String> {
        return try {
            val cursor = contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.TYPE),
                null,
                null,
                "${CallLog.Calls.DATE} DESC LIMIT 1"
            )
            cursor?.use {
                if (it.moveToFirst()) {
                    val number = it.getString(0)
                    val type = it.getInt(1)
                    val typeStr = when (type) {
                        CallLog.Calls.INCOMING_TYPE -> "Incoming call"
                        CallLog.Calls.MISSED_TYPE -> "Missed call"
                        CallLog.Calls.OUTGOING_TYPE -> "Outgoing call"
                        else -> "Call"
                    }
                    return Pair(number, typeStr)
                }
            }
            Pair("Unknown", "Call")
        } catch (e: Exception) {
            Log.e("NotificationLogger", "Failed to fetch call log", e)
            Pair("Unknown", "Call")
        }
    }
}
