package com.mashiverse.mashit.ui.screens.mashup.actions.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fitInside
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.Secondary
import com.mashiverse.mashit.ui.theme.SmallIconSize

@Composable
fun ColorSelectActionButton(
    onColor: () -> Unit
) {
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(90))
            .background(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color.Red,
                        Color.Yellow,
                        Color.Green,
                        Color.Blue,
                        Color.Red
                    ).reversed(),
                    center = Offset(
                        x = with(density) { 18.dp.toPx() },
                        y = with(density) { 18.dp.toPx() })
                )
            )
            .background(Color.Black.copy(alpha = 0.5F))
//            .border(1.dp, Secondary, RoundedCornerShape(90))
            .clickable { onColor.invoke() }
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.Center)
                .size(SmallIconSize),
            imageVector = Icons.Default.Brush,
            tint = ContentAccentColor,
            contentDescription = null
        )
    }
}