package com.mirchi.edclogger.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mirchi.edclogger.R
import com.mirchi.edclogger.ui.theme.Black
import com.mirchi.edclogger.ui.theme.Red
import com.mirchi.edclogger.ui.theme.Yellow
import com.mirchi.edclogger.utils.getCurrentRJName
import androidx.compose.ui.graphics.graphicsLayer

import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavHostController) {
    val currentRJ = getCurrentRJName()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val mascotSize = (screenWidth * 0.4).dp
    val padding = (screenWidth * 0.05).dp

    // Trigger one-time fade-in state
    var fadeIn by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fadeIn = true
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(padding),
        color = Black
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mascot
            Image(
                painter = painterResource(id = R.drawable.mirchi_chilli_static),
                contentDescription = "Mascot",
                modifier = Modifier
                    .size(mascotSize)
                    .graphicsLayer { alpha = if (fadeIn) 1f else 0f }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Texts
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Welcome, $currentRJ!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Yellow
                )

                Text(
                    text = "Track your RJ logs, insights,\nand engagement — all in one dashboard.",
                    fontSize = 14.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons
            Column {
                Button(
                    onClick = { navController.navigate("analytics") },
                    colors = ButtonDefaults.buttonColors(containerColor = Red),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("📊 Go to Analytics", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("automation") },
                    colors = ButtonDefaults.buttonColors(containerColor = Red),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("⚙️ Go to Automation", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

