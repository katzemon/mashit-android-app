package com.mashiverse.mashit.ui.default.indicators


import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import com.mashiverse.mashit.R

@Composable
fun SyncIndicator(modifier: Modifier = Modifier) {
    val visibilityProgress = rememberInfiniteTransition().animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(666
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Image(
        modifier = modifier.alpha(visibilityProgress.value),
        painter = painterResource(R.drawable.sync_icon),
        contentDescription = null
    )
}