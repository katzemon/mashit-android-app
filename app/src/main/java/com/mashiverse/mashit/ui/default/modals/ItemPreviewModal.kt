package com.mashiverse.mashit.ui.default.modals

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.OptionalTrait
import com.mashiverse.mashit.data.models.mashi.Trait
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.models.sys.screens.ScreenInfo
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.ui.default.grids.TraitHolderGrid
import com.mashiverse.mashit.ui.default.traits.MashupComposite
import com.mashiverse.mashit.ui.theme.BottomSheetShape
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.LargeHolderHeight
import com.mashiverse.mashit.ui.theme.LargeHolderWidth
import com.mashiverse.mashit.ui.theme.MediumPadding
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.ui.theme.Surface
import com.mashiverse.mashit.utils.helpers.sys.detectScreenType

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemPreviewModal(
    selectedNft: Nft,
    closeBottomSheet: () -> Unit,
    processImageIntent: (ImageIntent) -> Unit,
    sheetState: SheetState,
    detailsContent: @Composable () -> Unit
) {
    val config = LocalConfiguration.current
    val screenType = config.detectScreenType()

    val optionalTraits = remember(selectedNft) {
        selectedNft.traits?.map { trait ->
            OptionalTrait(trait = trait, selected = true)
        }?.toMutableStateList()
    }

    val selectTrait = { trait: Trait ->
        val index = optionalTraits?.indexOfFirst { it.trait == trait } ?: -1
        if (index != -1) {
            val item = optionalTraits!![index]
            if (item.trait.type != TraitType.BACKGROUND) {
                optionalTraits[index] = item.copy(selected = !item.selected)
            }
        }
    }

    Box {
        ModalBottomSheet(
            modifier = Modifier
                .then(
                    if (screenType == ScreenInfo.EXPANDED) {
                        Modifier
                            .padding(start = 328.dp)
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    } else {
                        Modifier
                            .fillMaxWidth()
                    }
                )
                .align(Alignment.BottomEnd)
                .fillMaxHeight(0.8f),
            shape = BottomSheetShape,
            onDismissRequest = closeBottomSheet,
            sheetState = sheetState,
            containerColor = Surface,
            contentColor = ContentColor,
            dragHandle = null,
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
            sheetGesturesEnabled = false
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Padding, end = Padding)
                    .padding(top = 16.dp)
                    .navigationBarsPadding(),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(Padding)) {
                    val selectedTraits =
                        optionalTraits?.filter { it.selected }?.map { it.trait } ?: emptyList()

                    MashupComposite(
                        modifier = Modifier
                            .height(LargeHolderHeight)
                            .width(LargeHolderWidth),
                        assets = selectedTraits,
                        holderWidth = LargeHolderWidth,
                        processImageIntent = processImageIntent
                    )

                    detailsContent()
                }

                Spacer(modifier = Modifier.height(Padding))

                optionalTraits?.let { traits ->
                    TraitHolderGrid(
                        items = traits,
                        spacedByHoriz = MediumPadding,
                        spacedByVert = Padding,
                        columns = screenType.collectionColumns,
                        processImageIntent = processImageIntent
                    ) { trait ->
                        selectTrait(trait)
                    }
                }
            }
        }
    }
}