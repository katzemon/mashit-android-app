package com.mashiverse.mashit.ui.screens.shop.regular

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.models.sys.data.ShopDataType
import com.mashiverse.mashit.data.states.shop.ShopIntent
import com.mashiverse.mashit.data.states.sys.DialogIntent
import com.mashiverse.mashit.ui.default.dialogs.Dialog
import com.mashiverse.mashit.ui.default.indicators.LoadingIndicator
import com.mashiverse.mashit.ui.default.modals.ItemPreviewModal
import com.mashiverse.mashit.ui.default.modals.MashiDetailsSection
import com.mashiverse.mashit.ui.screens.shop.sections.Category
import com.mashiverse.mashit.ui.screens.shop.sections.Drops
import com.mashiverse.mashit.ui.theme.Padding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularShop(
    searchQuery: State<String>,
    clearSearchQuery: () -> Unit,
    listingId: String?,
    navigateToDrop: (String) -> Unit
) {
    var searchQuery by remember(searchQuery.value) {
        mutableStateOf(searchQuery.value)
    }

    val previewState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val viewModel = hiltViewModel<RegularShopViewModel>()
    var clientRef by remember { mutableStateOf<CoinbaseWalletSDK?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data ?: return@rememberLauncherForActivityResult
        clientRef?.handleResponse(uri)
    }

    val specialDisabled = viewModel.specialDropsFlow.collectAsState(false)

    val categoryGState = rememberLazyGridState()
    var isHidden by remember { mutableStateOf(false) }
    LaunchedEffect(categoryGState.canScrollBackward) {
        isHidden = categoryGState.canScrollBackward
    }
    val specialDrops by remember { viewModel.specialDrops }

    LaunchedEffect(Unit) {
        clientRef = viewModel.getCoinbaseSdk { intent ->
            launcher.launch(intent)
        }
    }

    val shopUiState by remember { viewModel.shopUiState }

    LaunchedEffect(listingId) {
        if (!listingId.isNullOrEmpty()) {
            viewModel.processShopIntent(ShopIntent.OnNftSelect(listingId))
        }
    }

    LaunchedEffect(searchQuery) {
        val dataType = if (searchQuery.isNotEmpty()) {
            ShopDataType.SEARCH
        } else {
            ShopDataType.RECENTLY
        }

        viewModel.processShopIntent(ShopIntent.OnDataTypeSelect(dataType, searchQuery))
        if (searchQuery.isNotEmpty()) {
            viewModel.processShopIntent(ShopIntent.OnCategorySelect)
        }
    }

    var isLoaded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Padding)
    ) {
        AnimatedVisibility(searchQuery.isEmpty() && !isHidden && !specialDisabled.value && isLoaded) {
            Drops(
                specialDrops = specialDrops,
                navigateToDrop = navigateToDrop
            )
        }

        Category(
            shopUiState = shopUiState,
            clientRef = clientRef,
            categoryState = categoryGState,
            onSearchQueryClear = if (searchQuery.isNotEmpty()) {
                {
                    clearSearchQuery.invoke()
                    viewModel.processShopIntent(ShopIntent.OnCategoryClose)
                }
            } else null,
            processWeb3Intent = { intent -> viewModel.processWeb3Intent(intent) },
            processImageIntent = { intent -> viewModel.processImageIntent(intent) },
            processShopIntent = { intent -> viewModel.processShopIntent(intent) }
        ) { loaded ->
            isLoaded = loaded
        }
    }

    if (shopUiState.isLoadingPreview || shopUiState.selectedNft != null) {
        if (shopUiState.isLoadingPreview) {
            LoadingIndicator(text = "Loading")
        }

        shopUiState.selectedNft?.let { nft ->
            ItemPreviewModal(
                selectedNft = nft,
                sheetState = previewState,
                closeBottomSheet = { viewModel.processShopIntent(ShopIntent.OnNftDeselect) },
                processImageIntent = { intent -> viewModel.processImageIntent(intent) }
            ) {
                MashiDetailsSection(
                    nft = nft,
                    scope = scope,
                    closeBottomSheet = { viewModel.processShopIntent(ShopIntent.OnNftDeselect) },
                    sheetState = previewState,
                    clientRef = clientRef,
                    processWeb3Intent = { intent -> viewModel.processWeb3Intent(intent) }
                )
            }
        }
    }

    shopUiState.dialogContent?.let { content ->
        Dialog(content) {
            viewModel.processDialogIntent(DialogIntent.OnClear)
        }
    }
}