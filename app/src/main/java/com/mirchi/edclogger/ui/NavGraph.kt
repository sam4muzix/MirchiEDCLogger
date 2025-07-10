package com.mirchi.edclogger.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mirchi.edclogger.ui.screens.AnalyticsScreen
import com.mirchi.edclogger.ui.screens.AutomationScreen
import com.mirchi.edclogger.ui.screens.HomeScreen

@Composable
fun NavGraph(startDestination: String = "home") {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("analytics") {
            AnalyticsScreen(navController)
        }
        composable("automation") {
            AutomationScreen(navController)
        }
    }
}

