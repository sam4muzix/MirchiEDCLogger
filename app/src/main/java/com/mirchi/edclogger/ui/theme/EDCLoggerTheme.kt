package com.mirchi.edclogger.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MirchiColorScheme = darkColorScheme(
    primary = Color(0xFFFF1744),      // Red
    secondary = Color(0xFFFFD600),    // Yellow
    background = Color(0xFF000000),   // Black
    surface = Color(0xFF121212),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun EDCLoggerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MirchiColorScheme,
        typography = AppTypography, // ✅ use custom typography
        content = content
    )
}