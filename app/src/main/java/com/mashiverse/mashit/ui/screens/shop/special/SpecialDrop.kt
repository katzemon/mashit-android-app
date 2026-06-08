package com.mashiverse.mashit.ui.screens.shop.special

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.models.sys.screens.ScreenInfo
import com.mashiverse.mashit.data.states.shop.ShopIntent
import com.mashiverse.mashit.data.states.sys.DialogIntent
import com.mashiverse.mashit.ui.default.dialogs.Dialog
import com.mashiverse.mashit.ui.default.grids.ShopItemGrid
import com.mashiverse.mashit.ui.default.indicators.LoadingIndicator
import com.mashiverse.mashit.ui.default.modals.ItemPreviewModal
import com.mashiverse.mashit.ui.default.modals.MashiDetailsSection
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.MediumPadding
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.Secondary
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.utils.helpers.sys.detectScreenType
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialDrop(slug: String) {
    val config = LocalConfiguration.current
    val density = LocalDensity.current

    val screenType = config.detectScreenType()


    val viewModel = hiltViewModel<SpecialDropViewModel>()
    val specialDropUiState = viewModel.specialDropUiState

    LaunchedEffect(slug) {
        Timber.tag("GG").d(slug)
        viewModel.getDrop(slug)
    }

    val previewState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var clientRef by remember { mutableStateOf<CoinbaseWalletSDK?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data ?: return@rememberLauncherForActivityResult
        clientRef?.handleResponse(uri)
    }

    LaunchedEffect(Unit) {
        clientRef = viewModel.getCoinbaseSdk { intent ->
            launcher.launch(intent)
        }
    }

    var bannerHeight by remember { mutableStateOf(0.dp) }

    val gState = rememberLazyGridState()
    var isHidden by remember { mutableStateOf(false) }
    var isBio by remember { mutableStateOf(false) }
    var infoHeight by remember { mutableStateOf(0.dp) }

    val totalOffset by remember {
        derivedStateOf {
            val layoutInfo = gState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            if (visibleItemsInfo.isEmpty()) {
                0
            } else {
                val firstItem = visibleItemsInfo.first()
                val rowIndex = firstItem.index / screenType.shopColumns
                // We use the first item's height as the row height estimate
                (rowIndex * firstItem.size.height) + gState.firstVisibleItemScrollOffset
            }
        }
    }

    LaunchedEffect(totalOffset) {
        val currentOffsetDp = with(density) { totalOffset.toDp() }

        // Use a small buffer to prevent rapid toggling
        if (currentOffsetDp > infoHeight) {
            isHidden = true
        } else if (currentOffsetDp < infoHeight) {
            isBio = false
            isHidden = false
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Padding)
    ) {
        specialDropUiState.dropInfo?.let { info ->
            AnimatedVisibility(
                modifier = Modifier
                    .onSizeChanged { size ->
                        with(density) { infoHeight = size.height.toDp() }
                    },
                visible = !isHidden
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    val model =
                        if (screenType == ScreenInfo.COMPACT) info.mobileImageUrl else info.desktopImageUrl

                    AsyncImage(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .fillMaxWidth()
                            .heightIn(max = bannerHeight)
                            .clip(RoundedCornerShape(8))
                            .blur(radius = 20.dp) // Adjust blur intensity here
                            .alpha(0.33f),
                        model = model,
                        contentScale = ContentScale.FillWidth,
                        contentDescription = null
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .widthIn(max = 480.dp)
                    ) {
                        Box {
                            AsyncImage(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .fillMaxWidth()
                                    .onSizeChanged { size ->
                                        with(density) {
                                            bannerHeight = size.height.toDp()
                                        }
                                    }
                                    .clip(RoundedCornerShape(4)),
                                model = model,
                                contentDescription = null
                            )

                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(
                                        top = if (model.isNotEmpty()) max(
                                            0.dp,
                                            bannerHeight - 40.dp
                                        ) else 0.dp
                                    )
                            ) {
                                Spacer(modifier = Modifier.width(Padding))

                                Column {
                                    Spacer(modifier = Modifier.height(48.dp))

                                    IconButton(
                                        modifier = Modifier.size(32.dp), onClick = {
                                            isBio = !isBio
                                        }, colors = IconButtonDefaults.iconButtonColors().copy(
                                            containerColor = Secondary
                                        )
                                    ) {
                                        Icon(
                                            tint = ContentAccentColor,
                                            modifier = Modifier.size(24.dp),
                                            imageVector = if (isBio) Icons.Default.ArrowCircleUp else Icons.Default.ArrowCircleDown,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }

                        AnimatedVisibility(visible = isBio) {
                            Column {
                                Spacer(modifier = Modifier.height(SmallPadding))

                                Text(
                                    text = info.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ContentAccentColor
                                )

                                Text(
                                    text = info.description ?: "",
                                    fontSize = 14.sp,
                                    color = ContentColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(Padding))
                    }
                }
            }
        }

        ShopItemGrid(
            items = specialDropUiState.dropItems,
            state = gState,
            columns = screenType.shopColumns,
            spacedByHoriz = MediumPadding,
            spacedByVert = Padding,
            processShopIntent = { intent -> viewModel.processShopIntent(intent) },
            clientRef = clientRef,
            processImageIntent = { intent -> viewModel.processImageIntent(intent) },
            processWeb3Intent = { intent -> viewModel.processWeb3Intent(intent) },
        )
    }

    if (specialDropUiState.isLoadingPreview || specialDropUiState.selectedNft != null) {
        if (specialDropUiState.isLoadingPreview) {
            LoadingIndicator(text = "Loading")
        }

        specialDropUiState.selectedNft?.let { nft ->
            ItemPreviewModal(
                selectedNft = nft,
                sheetState = previewState,
                closeBottomSheet = { viewModel.processShopIntent(ShopIntent.OnNftDeselect) },
                processImageIntent = { intent -> viewModel.processImageIntent(intent) }
            ) {
                MashiDetailsSection(
                    nft = nft,
                    scope = scope,
                    closeBottomSheet = {
                        viewModel.processShopIntent(ShopIntent.OnNftDeselect)
                    },
                    sheetState = previewState,
                    clientRef = clientRef,
                    processWeb3Intent = { intent -> viewModel.processWeb3Intent(intent) }
                )
            }
        }
    }

    if (specialDropUiState.dropInfo == null) {
        LoadingIndicator(text = "Loading")
    }

    specialDropUiState.dialogContent?.let { content ->
        Dialog(content) {
            viewModel.processDialogIntent(DialogIntent.OnClear)
        }
    }
}