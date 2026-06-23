package com.mashiverse.mashit.ui.default.grids

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.mashi.OptionalTrait
import com.mashiverse.mashit.data.models.mashi.Trait
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.ui.default.traits.TraitHolder
import com.mashiverse.mashit.ui.theme.SmallPadding

@Composable
fun TraitHolderGrid(
    items: List<Trait>,
    spacedByVert: Dp,
    spacedByHoriz: Dp,
    columns: Int,
    processImageIntent: (ImageIntent) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacedByVert),
        horizontalArrangement = Arrangement.spacedBy(spacedByHoriz),
        columns = GridCells.Fixed(columns)
    ) {
        items(items.size) { i ->
            TraitHolder(
                trait = items[i],
                processImageIntent = processImageIntent,
                onClick = {}
            )
        }
    }
}

@Composable
fun TraitHolderGrid(
    items: List<OptionalTrait>,
    spacedByHoriz: Dp,
    spacedByVert: Dp,
    columns: Int,
    processImageIntent: (ImageIntent) -> Unit,
    onClick: (Trait) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacedByVert),
        horizontalArrangement = Arrangement.spacedBy(spacedByHoriz),
        columns = GridCells.Fixed(columns)
    ) {
        items(items.size) { i ->
            val isSelected = items[i].selected
            TraitHolder(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onClick.invoke(items[i].trait) },
                isSelected = isSelected,
                trait = items[i].trait,
                processImageIntent = processImageIntent
            )
        }
    }
}