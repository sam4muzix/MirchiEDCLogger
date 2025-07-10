package com.mirchi.edclogger

import android.content.Context
import android.provider.CallLog
import android.util.Log
import com.mirchi.edclogger.utils.GoogleSheetLogger
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object CallLogReader {

    private const val TAG = "CallLogReader"

    fun readAndExport(context: Context) {
        val resolver = context.contentResolver
        val cursor = try {
            resolver.query(
                CallLog.Calls.CONTENT_URI,
                null, null, null,
                CallLog.Calls.DATE + " DESC"
            )
        } catch (ex: SecurityException) {
            Log.e(TAG, "Permission READ_CALL_LOG not granted", ex)
            null
        }

        if (cursor == null) {
            Log.e(TAG, "Failed to query call logs or permission denied.")
            return
        }

        val logList = mutableListOf<String>()
        logList.add("timestamp,number,type")

        cursor.use {
            while (it.moveToNext()) {
                val number = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.NUMBER)) ?: "Unknown"
                val typeInt = it.getInt(it.getColumnIndexOrThrow(CallLog.Calls.TYPE))
                val date = it.getLong(it.getColumnIndexOrThrow(CallLog.Calls.DATE))
                val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(date))

                val typeStr = when (typeInt) {
                    CallLog.Calls.OUTGOING_TYPE -> "Outgoing call"
                    CallLog.Calls.INCOMING_TYPE -> "Incoming call"
                    CallLog.Calls.MISSED_TYPE -> "Missed call"
                    else -> "Other call"
                }

                logList.add("$dateStr,$number,$typeStr")

                // ⏱️ Determine RJ time and BeyondTime
                val cal = Calendar.getInstance()
                cal.timeInMillis = date
                val hour = cal.get(Calendar.HOUR_OF_DAY)
                val minute = cal.get(Calendar.MINUTE)
                val isBeyondTime = hour < 7 || (hour == 21 && minute > 0) || hour > 21
                val rjName = if (isBeyondTime) "" else getCurrentRJName(hour)

                // ✅ Send to Google Sheet logger with proper flags
                GoogleSheetLogger.logToSheet(
                    app = "CallLog",
                    rj = rjName,
                    contact = number,
                    messageOrCall = typeStr,
                    gender = "Unspecified",
                    location = "Unknown",
                    age = "N/A",
                    isBeyondTime = isBeyondTime
                )
            }
        }

        // Save CSV locally
        val outputDir = File(context.getExternalFilesDir(null), "exported_csv")
        if (!outputDir.exists()) outputDir.mkdirs()

        val fileName = "calls_${System.currentTimeMillis()}.csv"
        val outputFile = File(outputDir, fileName)
        try {
            outputFile.writeText(logList.joinToString("\n"))
            Log.i(TAG, "Call log CSV saved to ${outputFile.absolutePath}")
        } catch (ex: Exception) {
            Log.e(TAG, "Failed to write call log CSV", ex)
        }
    }

    // ⌛ RJ Mapping by Hour
    private fun getCurrentRJName(hour: Int): String {
        return when (hour) {
            in 7..10 -> "MirchiDQ"
            in 11..13 -> "MirchiJithu"
            in 14..16 -> "MirchiShivshankari"
            in 17..21 -> "MirchiJoe&Preethy"
            else -> "RJ NightBot"
        }
    }
}
