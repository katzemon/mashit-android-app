package com.mashiverse.mashit.ui.default.traits

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import coil3.size.Size
import com.mashiverse.mashit.ui.theme.Geist
import org.checkerframework.checker.units.qual.Speed
import org.checkerframework.checker.units.qual.s

@Composable
fun MintText(
    modifier: Modifier = Modifier,
    mint: Int,
    fontSize: Int? = null
) {
    val size = fontSize?.sp ?: 12.sp

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "#${mint}",
            fontSize = size,
            style = TextStyle(
                color = Color.Black, // The outline color
                drawStyle = Stroke(
                    width = 6f
                ),
            ),
            fontFamily = Geist,
        )

        // 2. Draw the FILL second (Top Layer)
        Text(
            text = "#${mint}",
            fontSize = size,
            color = Color.White // The inner color
        )
    }
}