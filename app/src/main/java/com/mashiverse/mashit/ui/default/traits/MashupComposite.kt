package com.mashiverse.mashit.ui.default.traits

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.mashiverse.mashit.data.models.mashi.Trait
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.ui.default.images.DefaultImage
import com.mashiverse.mashit.ui.default.indicators.LoadingIndicator
import com.mashiverse.mashit.ui.theme.TraitShape

@Composable
fun MashupComposite(
    modifier: Modifier = Modifier,
    assets: List<Trait>,
    colors: SelectedColors? = null,
    processImageIntent: (ImageIntent) -> Unit,
    holderWidth: Dp
) {
    Box(
        modifier = modifier
            .clip(TraitShape),
        contentAlignment = Alignment.Center
    ) {
        if (assets.none { it.url != null } || assets.isEmpty()) {
            LoadingIndicator()
        } else {
            assets.sortedBy { it.type }.forEach { trait ->
                val traitType = trait.type
                val width =
                    if (traitType == TraitType.BACKGROUND) holderWidth else holderWidth * 380 / 552
                val height =
                    if (traitType == TraitType.BACKGROUND) holderWidth * 4 / 3 else (holderWidth * 4 / 3) * 600 / 736
                val contentScale =
                    if (traitType == TraitType.BACKGROUND) ContentScale.FillBounds else ContentScale.Fit

                DefaultImage(
                    modifier = Modifier
                        .width(width)
                        .height(height),
                    data = trait.url ?: "",
                    processImageIntent = processImageIntent,
                    selectedColors = colors,
                    contentScale = contentScale,
                )
            }
        }
    }
}