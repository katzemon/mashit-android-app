package com.mashiverse.mashit.ui.default.grids

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.states.mashup.MashupIntent
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.ui.screens.mashup.traits.MashupTraitHolder
import com.mashiverse.mashit.ui.theme.SmallPadding

@Composable
fun MashupTraitHolderGrid(
    modifier: Modifier = Modifier,
    items: List<MashupTrait>,
    selectedTraitUrl: String,
    state: LazyGridState,
    spacedByVert: Dp,
    spacedByHoriz: Dp,
    columns: Int,
    processMashupIntent: (MashupIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit,
    isMoreTraits: Boolean,
    colors: SelectedColors
) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth(),
        state = state,
        verticalArrangement = Arrangement.spacedBy(spacedByVert),
        horizontalArrangement = Arrangement.spacedBy(spacedByHoriz),
        columns = GridCells.Fixed(columns)
    ) {
        items(items.size) { i ->
            MashupTraitHolder(
                isMoreTraits = isMoreTraits,
                isSelected = items[i].trait.url == selectedTraitUrl,
                mashupTrait = items[i],
                processMashupIntent = processMashupIntent,
                processImageIntent = processImageIntent,
                colors = colors,
                isPreview = true
            )
        }

        item {
            Spacer(Modifier.height(SmallPadding))
        }
    }
}