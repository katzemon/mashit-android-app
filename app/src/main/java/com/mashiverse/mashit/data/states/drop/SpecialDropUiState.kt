package com.mashiverse.mashit.data.states.drop

import com.mashiverse.mashit.data.models.drops.DropInfo
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.sys.dialog.DialogContent
import com.mashiverse.mashit.data.models.sys.wallet.WalletType

data class SpecialDropUiState(
    val wallet: String? = null,
    val walletType: WalletType = WalletType.BASE,
    val dropItems: List<Nft> = emptyList(),
    val dropInfo: DropInfo? = null,
    val selectedNft: Nft? = null,
    val dialogContent: DialogContent? = null,
    val isLoadingPreview: Boolean = false
)