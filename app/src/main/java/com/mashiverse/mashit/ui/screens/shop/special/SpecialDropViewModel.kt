package com.mashiverse.mashit.ui.screens.shop.special

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.models.drops.toInfo
import com.mashiverse.mashit.data.models.drops.toNfts
import com.mashiverse.mashit.data.models.sys.dialog.DialogContent
import com.mashiverse.mashit.data.models.sys.image.ImageType
import com.mashiverse.mashit.data.models.sys.wallet.WalletType
import com.mashiverse.mashit.data.repos.drops.DropsRepo
import com.mashiverse.mashit.data.repos.mashit.MashitRepo
import com.mashiverse.mashit.data.repos.sys.DatastoreRepo
import com.mashiverse.mashit.data.repos.sys.ImageTypeRepo
import com.mashiverse.mashit.data.repos.sys.Web3Repo
import com.mashiverse.mashit.data.states.drop.SpecialDropUiState
import com.mashiverse.mashit.data.states.shop.ShopIntent
import com.mashiverse.mashit.data.states.sys.DialogIntent
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.data.states.web3.Web3Intent
import com.mashiverse.mashit.utils.helpers.web3.SoldHelper
import com.mashiverse.mashit.utils.helpers.web3.Web3Helper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SpecialDropViewModel @Inject constructor(
    private val dropsRepo: DropsRepo,
    private val imageTypeRepo: ImageTypeRepo,
    private val dataStoreRepo: DatastoreRepo,
    private val web3Repo: Web3Repo,
    private val mashitRepo: MashitRepo
) : ViewModel() {
    var specialDropUiState by mutableStateOf(SpecialDropUiState())
        private set

    val walletFlow = dataStoreRepo.walletFlow

    init {
        observeWallet()
    }

    // Observers

    private fun observeWallet() {
        viewModelScope.launch(Dispatchers.IO) {
            walletFlow.distinctUntilChanged().collect { prefs ->
                val wallet = prefs.wallet
                val walletType = prefs.walletType

                if (!wallet.isNullOrEmpty()) {
                    specialDropUiState = specialDropUiState.copy(
                        wallet = wallet,
                        walletType = walletType
                    )
                } else {
                    specialDropUiState = specialDropUiState.copy(wallet = null)
                }
            }
        }
    }

    fun getDrop(slug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val drop = dropsRepo.getSingleDrop(slug)
            specialDropUiState = specialDropUiState.copy(
                dropInfo = drop.drop.toInfo(),
                dropItems = drop.drop.toNfts()
            )
        }
    }

    // Shop Intent

    fun processShopIntent(intent: ShopIntent) {
        when (intent) {
            is ShopIntent.OnNftSelect -> selectNft(intent.id)

            is ShopIntent.OnNftDeselect -> deselectNft()

            else -> {}
        }
    }

    private fun selectNft(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                specialDropUiState = specialDropUiState.copy(
                    isLoadingPreview = true
                )

                specialDropUiState = specialDropUiState.copy(
                    selectedNft = mashitRepo.getShopItem(id),
                    isLoadingPreview = false
                )
            } catch (e: Exception) {
                Timber.e(e, "Failed to fetch shop item details")
            }
        }
    }

    private fun deselectNft() {
        specialDropUiState = specialDropUiState.copy(
            selectedNft = null
        )
    }

    // Image Type

    fun processImageIntent(intent: ImageIntent) {
        when (intent) {
            is ImageIntent.OnTypeGet -> getImageType(intent.url, intent.onResult)

            is ImageIntent.OnTypeSet -> setImageType(intent.url, intent.type)
        }
    }

    private fun getImageType(url: String, onResult: (ImageType?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = imageTypeRepo.getImageType(url)?.type
            withContext(Dispatchers.Main) {
                onResult.invoke(result)
            }
        }
    }

    private fun setImageType(url: String, imageType: ImageType) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = ImageTypeEntity(url, imageType)
            imageTypeRepo.insertImageType(entity)
        }
    }

    // Dialog

    fun processDialogIntent(intent: DialogIntent) {
        when (intent) {
            is DialogIntent.OnClear -> specialDropUiState =
                specialDropUiState.copy(dialogContent = null)

            is DialogIntent.OnChange -> specialDropUiState =
                specialDropUiState.copy(dialogContent = intent.content)
        }
    }

    // Web3

    fun processWeb3Intent(intent: Web3Intent) {
        when (intent) {
            is Web3Intent.OnTotalSoldGet -> getTotalSold(
                listing = intent.listingId,
                callback = intent.callback
            )

            is Web3Intent.OnMint -> onMint(
                clientRef = intent.client,
                listingId = intent.listingId,
                price = intent.price,
                isPolCurrency = intent.isPolCurrency
            )
        }
    }

    private fun onMint(
        clientRef: CoinbaseWalletSDK?,
        listingId: String,
        price: Double,
        isPolCurrency: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. Pull the actual latest value from the Flow itself, not the UI snapshot
            val latestPrefs = dataStoreRepo.walletFlow.first()
            val address = latestPrefs.wallet
            val type = latestPrefs.walletType

            // 2. Switch to Main to update UI if needed
            withContext(Dispatchers.Main) {
                if (isPolCurrency) {
                    specialDropUiState = specialDropUiState.copy(
                        dialogContent = DialogContent(
                            title = "POL Currency",
                            text = "We currently don't support POL minting"
                        )
                    )
                } else if (!address.isNullOrEmpty()) {
                    // 3. Pass the fresh data to the mint function
                    mint(
                        client = clientRef,
                        walletType = type,
                        fromAddress = address,
                        listingId = listingId,
                        price = price
                    )
                } else {
                    specialDropUiState = specialDropUiState.copy(
                        dialogContent = DialogContent(
                            title = "Not Authenticated",
                            text = "Please connect your wallet to continue"
                        )
                    )
                }
            }
        }
    }

    fun getCoinbaseSdk(openIntent: (Intent) -> Unit): CoinbaseWalletSDK {
        return web3Repo.getCoinbaseSdk(openIntent)
    }

    private fun getTotalSold(listing: Int, callback: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val totalSold = SoldHelper.getTotalSold(listing.toLong()).toInt()
                withContext(Dispatchers.Main) {
                    callback.invoke(totalSold)
                }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    callback.invoke(0)
                }
            }
        }
    }

    private fun mint(
        client: CoinbaseWalletSDK?,
        walletType: WalletType,
        fromAddress: String,
        listingId: String,
        price: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Web3Helper.mint(
                walletType = walletType,
                client = client,
                fromAddress = fromAddress,
                listingId = listingId,
                price = price,
                onDialogTrigger = { dialogContent ->
                    // CRITICAL: Push UI updates back to the Main thread
                    viewModelScope.launch(Dispatchers.Main) {
                        specialDropUiState = specialDropUiState.copy(
                            dialogContent = dialogContent
                        )
                    }
                }
            )
        }
    }
}