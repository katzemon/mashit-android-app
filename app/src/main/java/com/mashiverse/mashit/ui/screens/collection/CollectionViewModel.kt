package com.mashiverse.mashit.ui.screens.collection

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.sys.image.ImageType
import com.mashiverse.mashit.data.repos.mashit.CollectionRepo
import com.mashiverse.mashit.data.repos.sys.DatastoreRepo
import com.mashiverse.mashit.data.repos.sys.ImageTypeRepo
import com.mashiverse.mashit.data.states.sys.ImageIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    collectionRepo: CollectionRepo,
    dataStoreRepo: DatastoreRepo,
    private val imageTypeRepo: ImageTypeRepo
) : ViewModel() {
    val walletPreferences = dataStoreRepo.walletFlow
    val collectionFlow = collectionRepo.collectionFlow

    val isSync = mutableStateOf(false)

    val isReady = mutableStateOf(false)

    private val _selectedNft = mutableStateOf<Nft?>(null)
    val selectedNft: State<Nft?> get() = _selectedNft

    init {
        viewModelScope.launch(Dispatchers.IO) {
            walletPreferences
                .distinctUntilChanged()
                .collect { prefs ->
                    val wallet = prefs.wallet

                    if (!wallet.isNullOrEmpty()) {
                        isReady.value = collectionFlow.first().isNotEmpty()
                        try {
                            isSync.value = true
                            collectionRepo.updateOwnedData(wallet)
                        } catch (e: Exception) {
                            Timber.tag("GG").e(e, "updateOwnedData failed")
                        } finally {
                            isSync.value = false
                            isReady.value = true
                        }
                    } else {
                        collectionRepo.clearOwned()
                    }
                }
        }
    }

    fun selectMashi(mashi: Nft) {
        _selectedNft.value = mashi
    }

    fun processImageIntent(intent: ImageIntent) {
        when (intent) {
            is ImageIntent.OnTypeGet -> getImageType(intent.url, intent.onResult)

            is ImageIntent.OnTypeSet -> setImageType(intent.url, intent.type)
        }
    }

    fun getImageType(url: String, onResult: (ImageType?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = imageTypeRepo.getImageType(url)?.type
            withContext(Dispatchers.Main) {
                onResult.invoke(result)
            }
        }
    }

    fun setImageType(url: String, imageType: ImageType) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = ImageTypeEntity(url, imageType)
            imageTypeRepo.insertImageType(entity)
        }
    }
}