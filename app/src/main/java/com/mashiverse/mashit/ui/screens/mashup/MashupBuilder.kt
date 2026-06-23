package com.mashiverse.mashit.ui.screens.mashup

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mashiverse.mashit.data.models.mashi.MintData
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.data.states.mashup.ActionsIntent
import com.mashiverse.mashit.data.states.sys.DialogIntent.OnClear
import com.mashiverse.mashit.ui.default.dialogs.Dialog
import com.mashiverse.mashit.ui.default.grids.MashupTraitHolderGrid
import com.mashiverse.mashit.ui.default.indicators.LoadingIndicator
import com.mashiverse.mashit.ui.default.indicators.NotConnected
import com.mashiverse.mashit.ui.default.indicators.SyncIndicator
import com.mashiverse.mashit.ui.default.sorting.Sorting
import com.mashiverse.mashit.ui.screens.mashup.actions.MashupActions
import com.mashiverse.mashit.ui.screens.mashup.categories.CategorySelector
import com.mashiverse.mashit.ui.screens.mashup.categories.sections.CollectiblesCategory
import com.mashiverse.mashit.ui.screens.mashup.color.ColorSheet
import com.mashiverse.mashit.ui.screens.mashup.mint.MintSelector
import com.mashiverse.mashit.ui.screens.mashup.preview.MashupPreview
import com.mashiverse.mashit.ui.theme.MediumPadding
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.ui.theme.Surface
import com.mashiverse.mashit.ui.theme.XLHolderHeight
import com.mashiverse.mashit.ui.theme.XLHolderWidth
import com.mashiverse.mashit.utils.color.helpers.toHexColor
import com.mashiverse.mashit.utils.helpers.nft.getTraitsByType
import com.mashiverse.mashit.utils.helpers.nft.sortNfts
import com.mashiverse.mashit.utils.helpers.sys.detectScreenType

@SuppressLint(
    "ConfigurationScreenWidthHeight", "FlowOperatorInvokedInComposition",
    "CoroutineCreationDuringComposition"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MashupBuilder(searchQuery: State<String>, openSettings: () -> Unit) {
    val searchQuery by remember(searchQuery.value) {
        mutableStateOf(searchQuery.value)
    }

    val config = LocalConfiguration.current
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val viewModel = hiltViewModel<MashupViewModel>()

    val screenType = config.detectScreenType()

    // Modals
    val colorChangingState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val previewState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val traitsGridState = rememberLazyGridState()
    val collectiblesVState = rememberLazyListState()

    var height by remember { mutableStateOf(0.dp) }

    val mashupUiState by remember { viewModel.mashupUiState }
    val mashupState by remember { viewModel.mashupState }


    val selectedColorType by remember(mashupState.selectedColorType) {
        mutableStateOf(
            mashupState.selectedColorType
        )
    }

    val currentColor = remember(selectedColorType, mashupState.colors) {
        when (selectedColorType) {
            ColorType.BASE -> mashupState.colors.base
            ColorType.EYES -> mashupState.colors.eyes
            ColorType.HAIR -> mashupState.colors.hair
        }
    }

    val previousColor = remember(mashupState.mashupDetails, selectedColorType) {
        when (selectedColorType) {
            ColorType.BASE -> mashupState.mashupDetails.colors.base
            ColorType.EYES -> mashupState.mashupDetails.colors.eyes
            ColorType.HAIR -> mashupState.mashupDetails.colors.hair
        }
    }

    var nfts by remember { mutableStateOf<List<Nft>>(emptyList()) }
    var mintData by remember { mutableStateOf<List<MintData>>(emptyList()) }

    LaunchedEffect(mashupState.nfts) {
        val tempData = mutableListOf<MintData>()
        mashupState.nfts.forEach { nft ->
            tempData.add(
                MintData(
                    compositeUrl = nft.traits?.first { it.type == TraitType.BACKGROUND }?.url ?: "",
                    mints = nft.owned!!.map { it.mint }
                )
            )
        }
        mintData = tempData
    }

    val availableMints by remember(mintData, mashupState.mashupDetails) {
        derivedStateOf {
            val backgroundUrl =
                mashupState.mashupDetails.assets.first { it.type == TraitType.BACKGROUND }.url
            val availableMints =
                if (mintData.firstOrNull { it.compositeUrl == backgroundUrl } != null) {
                    mintData.first { it.compositeUrl == backgroundUrl }.mints
                } else {
                    emptyList()
                }

            if (availableMints.isEmpty()) {
                viewModel.updateSelectedMint(null)
            }

            availableMints
        }
    }

    LaunchedEffect(mashupState.mashupDetails) {
        val selectedBackground =
            mashupState.mashupDetails.assets.first { it.type == TraitType.BACKGROUND }.url ?: ""
        val selectedNft =
            nfts.firstOrNull { it.traits?.firstOrNull { it.url == selectedBackground } != null }

        if (selectedNft == null) {
            viewModel.updateName(null)
        } else {
            viewModel.updateName(selectedNft.name)
        }
    }

    LaunchedEffect(mashupState.nfts, searchQuery) {
        val temp = mutableListOf<Nft>()
        mashupState.nfts.forEach { nft ->
            temp.add(nft)
        }

        nfts = if (searchQuery.isEmpty()) {
            temp
        } else {
            temp.filter {
                it.name.lowercase().contains(searchQuery.lowercase())
                        || it.author.lowercase().contains(searchQuery.lowercase())
            }
        }
    }

    val selectedTraitUrl by remember(mashupState.mashupDetails, mashupState.selectedCategory) {
        derivedStateOf {
            mashupState.mashupDetails.assets.first { it.type == mashupState.selectedCategory }.url
                ?: ""
        }
    }

    val sortedNfts = remember(mashupState.sortType, nfts) {
        sortNfts(mashupState.sortType, nfts)
    }

    val traits by remember(mashupState.selectedCategory, sortedNfts) {
        derivedStateOf {
            val traits =
                getTraitsByType(sortedNfts)[mashupState.selectedCategory]
                    ?: emptyList()

            if (mashupState.selectedCategory != TraitType.BACKGROUND) {
                traits.distinctBy { it.avatarName }
            } else {
                traits
            }
        }
    }

    LaunchedEffect(mashupUiState.isCollectibles) {
        if (mashupUiState.isCollectibles) {
            collectiblesVState.animateScrollToItem(0)
        }
    }

    val isSync by remember { viewModel.isSync }

    Column {
        if (mashupState.wallet != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Padding)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (isSync) {
                        SyncIndicator(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.TopStart)
                        )
                    }

                    MashupActions(
                        mashupDetails = mashupState.mashupDetails.copy(colors = mashupState.colors),
                        modifier = Modifier
                            .height(XLHolderHeight)
                            .width(XLHolderWidth)
                            .clickable { viewModel.processActionsIntent(ActionsIntent.OnPreview) },
                        holderWidth = XLHolderWidth,
                        processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                        processActionsIntent = { intent -> viewModel.processActionsIntent(intent) },
                        selectedMint = mashupState.selectedMint
                    )

                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(40.dp),
                        colors = IconButtonDefaults.iconButtonColors()
                            .copy(containerColor = Surface),
                        onClick = openSettings
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(SmallPadding))


                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .onSizeChanged { size ->
                            height = with(density) { size.height.toDp() } + 64.dp
                        },
                ) {
                    if (mashupUiState.isCollectionReady) {
                        if (mashupUiState.isCollectibles) {
                            CollectiblesCategory(
                                modifier = Modifier.weight(1F),
                                nfts = sortedNfts,
                                mashupDetails = mashupState.mashupDetails,
                                state = collectiblesVState,
                                scope = scope,
                                processMashupIntent = { intent ->
                                    viewModel.processMashupIntent(
                                        intent
                                    )
                                },
                                processImageIntent = { intent -> viewModel.processImageIntent(intent) }
                            )
                        } else {
                            MashupTraitHolderGrid(
                                modifier = Modifier.weight(1f),
                                items = traits,
                                selectedTraitUrl = selectedTraitUrl,
                                state = traitsGridState,
                                spacedByHoriz = MediumPadding,
                                spacedByVert = MediumPadding,
                                columns = screenType.collectionColumns,
                                processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                                processMashupIntent = { intent ->
                                    viewModel.processMashupIntent(
                                        intent
                                    )
                                }
                            )
                        }

                        Row(
                            modifier = Modifier
                                .padding(vertical = SmallPadding),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MintSelector(
                                modifier = Modifier,
                                mints = availableMints,
                                selectedMint = mashupState.selectedMint
                            ) { mint ->
                                viewModel.updateSelectedMint(mint)
                            }

                            Sorting { type ->
                                viewModel.changeSortType(
                                    scope = scope,
                                    vState = collectiblesVState,
                                    gState = traitsGridState,
                                    type = type
                                )
                            }

                            CategorySelector(
                                mashupState = mashupState,
                                mashupUiState = mashupUiState,
                                processMashupIntent = { intent ->
                                    viewModel.processMashupIntent(
                                        intent
                                    )
                                },
                                gridState = traitsGridState,
                                scope = scope
                            )
                        }
                    } else {
                        LoadingIndicator(text = "Loading")
                    }
                }
            }
        } else {
            NotConnected()
        }

        if (mashupUiState.isColorChange) {
            ColorSheet(
                sheetState = colorChangingState,
                initialColor = previousColor.toHexColor(),
                color = currentColor.toHexColor(),
                scope = scope,
                selectedColorType = selectedColorType,
                processMashupIntent = { intent -> viewModel.processMashupIntent(intent) },
                processActionsIntent = { intent -> viewModel.processActionsIntent(intent) },
                height = height
            )
        }

        if (mashupUiState.isPreview) {
            MashupPreview(
                closeBottomSheet = { viewModel.processActionsIntent(ActionsIntent.OnPreviewDismiss) },
                sheetState = previewState,
                scope = scope,
                mashupDetails = mashupState.mashupDetails.copy(colors = mashupState.colors),
                processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                height = height
            )
        }
    }

    if (mashupUiState.isDownloading) {
        LoadingIndicator("Downloading")
    }

    if (mashupUiState.isSave) {
        LoadingIndicator("Saving")
    }

    mashupUiState.dialogContent?.let { content ->
        Dialog(content) {
            viewModel.processDialogIntent(OnClear)
        }
    }
}