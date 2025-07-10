package com.mirchi.edclogger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.mirchi.edclogger.utils.AnalyticsApiHelper

enum class ChartType { CALLS, MESSAGES }

data class LeaderboardEntry(val rj: String, val count: Int)

class AnalyticsViewModel : ViewModel() {
    private val _chartData = MutableStateFlow<List<Int>>(emptyList())
    val chartData: StateFlow<List<Int>> = _chartData.asStateFlow()

    private val _leaderboard = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val leaderboard: StateFlow<List<LeaderboardEntry>> = _leaderboard.asStateFlow()

    val rjNames = listOf("MirchiDQ", "MirchiJithu", "MirchiShivshankari", "MirchiJoe&Preethy", "RJ NightBot")

    fun loadData(timeRange: String, chartType: ChartType) {
        viewModelScope.launch {
            // Fetch data from backend (Google Sheet API, etc)
            val apiData = AnalyticsApiHelper.fetchAnalytics(timeRange, chartType)
            _chartData.value = apiData.chart
            _leaderboard.value = apiData.leaderboard
        }
    }
}
