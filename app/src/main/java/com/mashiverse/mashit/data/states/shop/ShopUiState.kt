package com.mashiverse.mashit.data.states.shop

import androidx.paging.PagingData
import com.mashiverse.mashit.data.models.sys.data.ShopDataType
import com.mashiverse.mashit.data.models.sys.dialog.DialogContent
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.sys.wallet.WalletType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class ShopUiState(
    val wallet: String? = null,
    val walletType: WalletType = WalletType.BASE,
    val itemsData: Flow<PagingData<Nft>> = flowOf(PagingData.Companion.empty()),
    val isCategory: Boolean = false,
    val category: ShopDataType = ShopDataType.RECENTLY,
    val selectedNft: Nft? = null,
    val isLoadingPreview: Boolean = false,
    val dialogContent: DialogContent? = null
)