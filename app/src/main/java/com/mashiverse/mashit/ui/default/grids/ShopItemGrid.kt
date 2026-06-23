package com.mashiverse.mashit.ui.default.grids

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.states.shop.ShopIntent
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.data.states.web3.Web3Intent
import com.mashiverse.mashit.ui.default.indicators.SectionLoading
import com.mashiverse.mashit.ui.default.indicators.SectionRefresh
import com.mashiverse.mashit.ui.screens.shop.regular.ShopItem
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Secondary
import com.mashiverse.mashit.ui.theme.SmallPadding

@Composable
fun ShopItemGrid(
    items: List<Nft>,
    state: LazyGridState,
    spacedByVert: Dp,
    spacedByHoriz: Dp,
    columns: Int,
    clientRef: CoinbaseWalletSDK?,
    processShopIntent: (ShopIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit,
    processWeb3Intent: (Web3Intent) -> Unit
) {
    LazyVerticalGrid(
        state = state,
        columns = GridCells.Fixed(columns),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(spacedByHoriz),
        verticalArrangement = Arrangement.spacedBy(spacedByVert)
    ) {
        items(
            items.size
        ) { index ->
            val nft = items[index]

            ShopItem(
                nft = nft,
                processShopIntent = processShopIntent,
                clientRef = clientRef,
                processImageIntent = processImageIntent,
                processWeb3Intent = processWeb3Intent,
            )
        }
    }
}

@Composable
fun ShopItemGrid(
    items: LazyPagingItems<Nft>,
    appendState: LoadState,
    state: LazyGridState,
    columns: Int,
    spacedByVert: Dp,
    clientRef: CoinbaseWalletSDK?,
    spacedByHoriz: Dp,
    processShopIntent: (ShopIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit,
    processWeb3Intent: (Web3Intent) -> Unit,
    isSearch: Boolean = false,
    isRefreshing: Boolean = false
) {
    LazyVerticalGrid(
        state = state,
        columns = GridCells.Fixed(columns),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(spacedByHoriz),
        verticalArrangement = Arrangement.spacedBy(spacedByVert)
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey { it.name + it.compositeUrl }
        ) { index ->
            val nft = items[index]
            nft?.let {
                ShopItem(
                    nft = nft,
                    processShopIntent = processShopIntent,
                    clientRef = clientRef,
                    processImageIntent = processImageIntent,
                    processWeb3Intent = processWeb3Intent,
                )
            }
        }

        if (isSearch && isRefreshing && items.itemCount == 0) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .clip(RoundedCornerShape(24))
                        .background(Secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nothing found",
                        fontSize = 14.sp,
                        color = ContentColor
                    )
                }
            }
        }

        if (appendState is LoadState.Loading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SectionLoading()
            }
        }

        if (appendState is LoadState.Error) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SectionRefresh(onRetry = { items.retry() })
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(Modifier.height(0.dp))
        }
    }
}