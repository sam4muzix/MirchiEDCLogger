package com.mirchi.edclogger.ui.screens

import android.media.MediaPlayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mirchi.edclogger.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val context = LocalContext.current
    var visible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1200),
        label = "fadeIn"
    )

    LaunchedEffect(Unit) {
        visible = true

        val player = MediaPlayer.create(context, R.raw.mirchi_whistle)
        player.setOnCompletionListener {
            it.release() // ✅ prevent memory leaks and system warnings
        }
        player.start()

        delay(2800)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_500x500_px),
            contentDescription = "Mirchi Splash Logo",
            modifier = Modifier
                .size(200.dp)
                .alpha(alpha)
        )
    }
}
