package com.mashiverse.mashit.ui.default.traits

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.ui.default.images.DefaultImage

@Composable
fun MintedTrait(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    data: String,
    processImageIntent: (ImageIntent) -> Unit,
    selectedColors: SelectedColors? = null,
    contentScale: ContentScale = ContentScale.Fit,
    mint: Int? = null
) {
    Box(
        modifier = modifier
    ) {
        DefaultImage(
            modifier = modifier
                .aspectRatio(3f / 4f),
            onClick = onClick,
            data = data,
            processImageIntent = processImageIntent,
            selectedColors = selectedColors,
            contentScale = contentScale,
        )

        mint?.let {
            MintText(
                modifier = Modifier
                    .padding(bottom = 3.dp, start = 8.dp)
                    .align(Alignment.BottomStart),
                mint = mint
            )
        }
    }
}