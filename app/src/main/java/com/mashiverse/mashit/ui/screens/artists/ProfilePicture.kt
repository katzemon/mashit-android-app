package com.mashiverse.mashit.ui.screens.artists

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.artists.ArtistMashup
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.ui.default.images.DefaultImage
import com.mashiverse.mashit.ui.theme.ContentAccentColor

@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    artistMashup: ArtistMashup,
    processImageIntent: (ImageIntent) -> Unit,
    size: Dp = 80.dp,
    borderWidth: Dp = 2.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .border(
                width = (borderWidth),
                color = ContentAccentColor,
                shape = CircleShape
            )
            .clip(CircleShape)
    ) {
        artistMashup.layers.forEach { url ->
            DefaultImage(
                modifier = Modifier.matchParentSize(),
                onClick = onClick,
                data = url,
                processImageIntent = processImageIntent,
                selectedColors = artistMashup.colors,
                contentScale = ContentScale.Crop,
            )
        }
    }
}
