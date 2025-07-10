package com.mirchi.edclogger.utils

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object GoogleSheetLogger {

    private const val SHEET_URL = "https://script.google.com/macros/s/AKfycbyRxr0YXv4V9dIDm4kgbzjFK7JEio-WJq8FKMGZ5uhRl4HUkW5ZsbYJv2Tl_PigNoQ/exec"
    private val client = OkHttpClient()

    private var lastLoggedHash: Int? = null

    fun logToSheet(
        app: String,
        rj: String,
        contact: String,
        messageOrCall: String,
        gender: String,
        location: String,
        age: String,
        isBeyondTime: Boolean = false
    ) {
        // 🛑 Ignore system auto logs
        if (app.equals("System", ignoreCase = true)) {
            Log.i("GoogleSheetLogger", "⛔ Skipped system auto log: $messageOrCall")
            return
        }

        val logFingerprint = "$app|$rj|$contact|$messageOrCall|$gender|$location|$age"
        val hash = logFingerprint.hashCode()
        if (hash == lastLoggedHash) {
            Log.i("GoogleSheetLogger", "🔁 Duplicate log. Skipping.")
            return
        }
        lastLoggedHash = hash

        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

        if (isBeyondTime) {
            // 🕗 Log to "BeyondTime"
            val beyondJson = buildJson(
                sheet = "BeyondTime",
                date = date,
                time = time,
                app = app,
                rj = "",
                contact = contact,
                messageOrCall = messageOrCall,
                gender = gender,
                location = location,
                age = age
            )
            sendLog(beyondJson)
        } else {
            // 📄 Log to RJ-specific sheet
            val rjJson = buildJson(
                sheet = rj,
                date = date,
                time = time,
                app = app,
                rj = rj,
                contact = contact,
                messageOrCall = messageOrCall,
                gender = gender,
                location = location,
                age = age
            )
            sendLog(rjJson)

            // 📊 Also log to "Today"
            val todayJson = JSONObject(rjJson.toString()).apply {
                put("Sheet", "Today")
                put("RJ Name", rj)
            }
            sendLog(todayJson)
        }
    }

    private fun buildJson(
        sheet: String,
        date: String,
        time: String,
        app: String,
        rj: String,
        contact: String,
        messageOrCall: String,
        gender: String,
        location: String,
        age: String
    ): JSONObject {
        return JSONObject().apply {
            put("Date", date)
            put("Time", time)
            put("App", app)
            put("RJ Name", rj)
            put("Contact", contact)
            put("Message/Call", messageOrCall)
            put("Gender", gender)
            put("Location", location)
            put("Age", age)
            put("Sheet", sheet)
        }
    }

    private fun sendLog(json: JSONObject) {
        Log.d("GoogleSheetLogger", "📤 Sending log: $json")

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(SHEET_URL)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GoogleSheetLogger", "❌ Failed: ${e.message}", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        Log.e("GoogleSheetLogger", "⚠️ Failed with code: ${it.code}")
                    } else {
                        Log.i("GoogleSheetLogger", "✅ Log success")
                    }
                }
            }
        })
    }
}
