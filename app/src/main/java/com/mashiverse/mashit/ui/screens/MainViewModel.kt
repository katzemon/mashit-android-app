package com.mashiverse.mashit.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.models.sys.dialog.DialogContent
import com.mashiverse.mashit.data.models.sys.image.ImageType
import com.mashiverse.mashit.data.models.sys.wallet.WalletPreferences
import com.mashiverse.mashit.data.repos.mashit.CollectionRepo
import com.mashiverse.mashit.data.repos.sys.DatastoreRepo
import com.mashiverse.mashit.data.repos.sys.ImageTypeRepo
import com.mashiverse.mashit.data.states.sys.ImageIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepo: DatastoreRepo,
    private val collectionRepo: CollectionRepo,
    private val imageTypeRepo: ImageTypeRepo
) : ViewModel() {
    val walletPreferences = dataStoreRepo.walletFlow
    val firstLaunchPreferences = dataStoreRepo.firstLaunchFlow
    val mashupFlow = walletPreferences.flatMapLatest {
        collectionRepo.getCachedMashupFlow(it.wallet ?: "")
    }

    private val _dialogContent = mutableStateOf<DialogContent?>(null)
    val dialogContent: State<DialogContent?> = _dialogContent

    fun clearDialog() {
        _dialogContent.value = null
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
            withContext(Dispatchers.Main) { onResult(result) }
        }
    }

    fun setImageType(url: String, imageType: ImageType) {
        viewModelScope.launch(Dispatchers.IO) {
            imageTypeRepo.insertImageType(ImageTypeEntity(url, imageType))
        }
    }

    fun setDialogContent(dialogContent: DialogContent) {
        _dialogContent.value = dialogContent
    }

    fun updateWallet(walletPreferences: WalletPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.updateWallet(walletPreferences)
        }
    }

    fun setFirstLaunchCompleted() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.setFirstLaunchCompleted()
        }
    }
}