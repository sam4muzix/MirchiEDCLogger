package com.mirchi.edclogger.ui.screens

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.mirchi.edclogger.ui.theme.Black
import com.mirchi.edclogger.ui.theme.Red
import com.mirchi.edclogger.ui.theme.Yellow
import com.mirchi.edclogger.viewmodel.AnalyticsViewModel
import com.mirchi.edclogger.viewmodel.ChartType

@Composable
fun AnalyticsScreen(navController: NavHostController, viewModel: AnalyticsViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(0) }
    var chartType by remember { mutableStateOf(ChartType.CALLS) }
    val tabs = listOf("Today", "Week", "Month")

    val chartData by viewModel.chartData.collectAsState()
    val leaderboard by viewModel.leaderboard.collectAsState()

    LaunchedEffect(selectedTab, chartType) {
        viewModel.loadData(tabs[selectedTab].lowercase(), chartType)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(16.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Black,
            contentColor = Yellow
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        viewModel.loadData(title.lowercase(), chartType)
                    },
                    text = {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            color = if (selectedTab == index) Yellow else Red
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .toggleable(
                    value = chartType == ChartType.CALLS,
                    onValueChange = {
                        chartType = if (chartType == ChartType.CALLS) ChartType.MESSAGES else ChartType.CALLS
                        viewModel.loadData(tabs[selectedTab].lowercase(), chartType)
                    }
                )
        ) {
            Text(
                text = if (chartType == ChartType.CALLS) "Calls" else "Messages",
                color = Yellow,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        AndroidView(
            factory = { ctx ->
                BarChart(ctx).apply {
                    setFitBars(true)
                    setDrawGridBackground(false)
                    description.isEnabled = false
                    legend.isEnabled = false
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.setDrawGridLines(false)
                    xAxis.granularity = 1f
                    axisLeft.setDrawGridLines(false)
                    axisRight.isEnabled = false
                    setBackgroundColor(Color.BLACK)
                }
            },
            update = { chart ->
                val entries = chartData.mapIndexed { index, count ->
                    BarEntry(index.toFloat(), count.toFloat())
                }
                val dataSet = BarDataSet(entries, "Logs").apply {
                    color = Yellow.toArgb()
                }

                chart.data = BarData(dataSet)
                chart.xAxis.valueFormatter = IndexAxisValueFormatter(viewModel.rjNames)
                chart.invalidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Top 5 RJs", color = Yellow, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        leaderboard.take(5).forEachIndexed { index, entry ->
            Text(
                text = "${index + 1}. ${entry.rj} - ${entry.count}",
                color = Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}
