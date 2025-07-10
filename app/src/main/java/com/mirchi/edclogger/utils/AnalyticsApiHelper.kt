package com.mirchi.edclogger.utils

import com.mirchi.edclogger.viewmodel.ChartType
import com.mirchi.edclogger.viewmodel.LeaderboardEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

data class AnalyticsData(
    val chart: List<Int>,
    val leaderboard: List<LeaderboardEntry>
)

object AnalyticsApiHelper {
    private val client = OkHttpClient()

    // Replace with your deployed Google Apps Script API endpoint
    private const val SHEET_API_URL =
        "https://script.google.com/macros/s/AKfycbyRxr0YXv4V9dIDm4kgbzjFK7JEio-WJq8FKMGZ5uhRl4HUkW5ZsbYJv2Tl_PigNoQ/exec  "

    suspend fun fetchAnalytics(timeRange: String, chartType: ChartType): AnalyticsData =
        withContext(Dispatchers.IO) {
            val url = "$SHEET_API_URL?sheet=$timeRange".trim()
            val request = Request.Builder().url(url).build()

            val response = client.newCall(request).execute()
            val bodyString = response.body?.string()?.trim() ?: ""

            // 🛑 Check if response is a valid JSON array
            if (!bodyString.startsWith("[") && !bodyString.startsWith("{")) {
                println("❌ Invalid JSON response from $url:\n$bodyString")
                return@withContext AnalyticsData(emptyList(), emptyList())
            }

            val jsonArray = try {
                JSONArray(bodyString)
            } catch (e: Exception) {
                println("❌ JSON parsing error: ${e.localizedMessage}")
                println("❗ Response content: $bodyString")
                return@withContext AnalyticsData(emptyList(), emptyList())
            }

            val rjNames = listOf(
                "MirchiDQ",
                "MirchiJithu",
                "MirchiShivshankari",
                "MirchiJoe&Preethy",
                "RJ NightBot"
            )
            val rjCounts = mutableMapOf<String, Int>().apply { rjNames.forEach { this[it] = 0 } }

            for (i in 0 until jsonArray.length()) {
                val log = jsonArray.optJSONObject(i) ?: continue
                val rj = log.optString("RJ Name", "RJ NightBot")
                val app = log.optString("App", "")
                if ((chartType == ChartType.CALLS && app == "Call") ||
                    (chartType == ChartType.MESSAGES && app == "WhatsApp")
                ) {
                    rjCounts[rj] = (rjCounts[rj] ?: 0) + 1
                }
            }

            val chart = rjNames.map { rjCounts[it] ?: 0 }
            val leaderboard = rjNames.map { LeaderboardEntry(it, rjCounts[it] ?: 0) }
                .sortedByDescending { it.count }

            AnalyticsData(chart = chart, leaderboard = leaderboard)
        }
}
