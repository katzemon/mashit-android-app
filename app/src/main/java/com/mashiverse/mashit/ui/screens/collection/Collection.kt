package com.mashiverse.mashit.ui.screens.collection

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.Owned
import com.mashiverse.mashit.data.models.mashi.SortType
import com.mashiverse.mashit.data.models.mashi.mappers.fromEntities
import com.mashiverse.mashit.data.models.sys.wallet.WalletPreferences
import com.mashiverse.mashit.ui.default.buttons.DiscordButton
import com.mashiverse.mashit.ui.default.buttons.RedditButton
import com.mashiverse.mashit.ui.default.grids.MintedTraitGrid
import com.mashiverse.mashit.ui.default.indicators.LoadingIndicator
import com.mashiverse.mashit.ui.default.indicators.NotConnected
import com.mashiverse.mashit.ui.default.indicators.SyncIndicator
import com.mashiverse.mashit.ui.default.modals.ItemPreviewModal
import com.mashiverse.mashit.ui.default.modals.MashiDetailsSection
import com.mashiverse.mashit.ui.default.sorting.Sorting
import com.mashiverse.mashit.ui.theme.MediumPadding
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.utils.helpers.nft.sortNfts
import com.mashiverse.mashit.utils.helpers.sys.detectScreenType
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@SuppressLint("ConfigurationScreenWidthHeight", "FlowOperatorInvokedInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Collection(searchQuery: State<String>, onSignIn: () -> Unit) {
    val config = LocalConfiguration.current
    val screenType = config.detectScreenType()

    val searchQuery by remember(searchQuery.value) {
        mutableStateOf(searchQuery.value)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<CollectionViewModel>()
    val collection by viewModel.collectionFlow
        .map { it.fromEntities() }
        .collectAsState(emptyList())

    var ownedNfts by remember { mutableStateOf<List<Nft>>(emptyList()) }

    val isReady by remember { viewModel.isReady }

    LaunchedEffect(collection, searchQuery) {
        val nfts = mutableListOf<Nft>()
        collection.forEach { nft ->
            nft.owned?.forEach { owned ->
                nfts.add(
                    nft.copy(
                        owned = listOf(
                            Owned(mint = owned.mint, timestamp = owned.timestamp)
                        )
                    )
                )
            }
        }
        ownedNfts = if (searchQuery.isEmpty()) {
            nfts
        } else {
            nfts.filter {
                it.name.lowercase().contains(searchQuery.lowercase())
                        || it.author.lowercase().contains(searchQuery.lowercase())
            }
        }
    }

    var sortType by remember { mutableStateOf(SortType.NEWEST) }

    val sortedNfts = remember(sortType, ownedNfts) {
        sortNfts(sortType, ownedNfts)
    }

    val lazyGridState = rememberLazyGridState()

    val walletPreferences = viewModel.walletPreferences.collectAsState(WalletPreferences(null))

    val selectedMashi: Nft? by remember {
        derivedStateOf {
            viewModel.selectedNft.value
        }
    }
    val selectMashi = { mashi: Nft ->
        viewModel.selectMashi(mashi)
        isBottomSheet = true
    }

    val closeBottomSheet = {
        isBottomSheet = false
    }

    if (walletPreferences.value.wallet != null) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Padding),
            ) {
                if (isReady) {
                    MintedTraitGrid(
                        modifier = Modifier.weight(1f),
                        items = sortedNfts,
                        state = lazyGridState,
                        spacedByHoriz = MediumPadding,
                        spacedByVert = MediumPadding,
                        columns = screenType.collectionColumns,
                        processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                    ) { nft ->
                        selectMashi.invoke(nft)
                    }

                    Spacer(modifier = Modifier.height(SmallPadding))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Sorting(onSortChange = { type ->
                            sortType = type
                            scope.launch { lazyGridState.animateScrollToItem(0) }
                        })

                        Spacer(modifier = Modifier.weight(1F))

                        DiscordButton()

                        Spacer(modifier = Modifier.width(SmallPadding))

                        RedditButton()
                    }

                    Spacer(modifier = Modifier.height(SmallPadding))
                } else {
                    LoadingIndicator(text = "Loading")
                }
            }

            if (viewModel.isSync.value) {
                SyncIndicator(
                    modifier = Modifier
                        .padding(start = Padding)
                        .size(40.dp)
                        .align(Alignment.TopStart)
                )
            }
        }

        if (isBottomSheet) {
            selectedMashi?.let {
                ItemPreviewModal(
                    selectedNft = selectedMashi!!,
                    closeBottomSheet = closeBottomSheet,
                    processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                    sheetState = sheetState,
                ) {
                    MashiDetailsSection(
                        nft = selectedMashi!!,
                        scope = scope,
                        closeBottomSheet = closeBottomSheet,
                        sheetState = sheetState,
                    )
                }
            }
        }
    } else {
        NotConnected(onSignIn = onSignIn)
    }
}