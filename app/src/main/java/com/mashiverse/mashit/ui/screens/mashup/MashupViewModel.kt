package com.mashiverse.mashit.ui.screens.mashup

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.models.mashi.SortType
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.models.mashi.mappers.fromEntities
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.data.models.mashup.generation.GenerateMashupReq
import com.mashiverse.mashit.data.models.mashup.save.MashupColors
import com.mashiverse.mashit.data.models.mashup.save.MashupLayer
import com.mashiverse.mashit.data.models.sys.dialog.DialogContent
import com.mashiverse.mashit.data.models.sys.image.DownloadType
import com.mashiverse.mashit.data.models.sys.image.ImageType
import com.mashiverse.mashit.data.repos.mashit.CollectionRepo
import com.mashiverse.mashit.data.repos.mashit.MashitRepo
import com.mashiverse.mashit.data.repos.sys.DatastoreRepo
import com.mashiverse.mashit.data.repos.sys.ImageTypeRepo
import com.mashiverse.mashit.data.states.mashup.ActionsIntent
import com.mashiverse.mashit.data.states.mashup.MashupIntent
import com.mashiverse.mashit.data.states.mashup.MashupState
import com.mashiverse.mashit.data.states.mashup.MashupUiState
import com.mashiverse.mashit.data.states.sys.DialogIntent
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.data.states.utils.StackManager
import com.mashiverse.mashit.utils.color.helpers.toHexString
import com.mashiverse.mashit.utils.helpers.nft.getRandomTraits
import com.mashiverse.mashit.utils.helpers.nft.toIpfsUri
import com.mashiverse.mashit.utils.helpers.sys.startImageDownload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MashupViewModel @Inject constructor(
    private val worker: WorkManager,
    val collectionRepo: CollectionRepo,
    dataStoreRepo: DatastoreRepo,
    private val mashitRepo: MashitRepo,
    private val imageTypeRepo: ImageTypeRepo
) : ViewModel() {

    val downloadWorkInfo = worker.getWorkInfosForUniqueWorkFlow("image_download_work")
        .map { it.firstOrNull() }

    var mashupUiState = mutableStateOf(MashupUiState())
        private set

    var mashupState = mutableStateOf(MashupState())
        private set

    private val stackManager = StackManager<MashupDetails>()

    private val walletFlow = dataStoreRepo.walletFlow
    private val collectionFlow = collectionRepo.collectionFlow

    var isSync = mutableStateOf(false)
        private set

    init {
        observeWallet()
        observeCollection()
        observeDownloadStatus()
    }

    // Observers
    private fun observeDownloadStatus() {
        viewModelScope.launch {
            downloadWorkInfo.collect { info ->
                when (info?.state) {
                    WorkInfo.State.RUNNING -> {
                        mashupUiState.value = mashupUiState.value.copy(isDownloading = true)
                    }

                    WorkInfo.State.SUCCEEDED, WorkInfo.State.FAILED, WorkInfo.State.CANCELLED -> {
                        mashupUiState.value = mashupUiState.value.copy(isDownloading = false)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun observeWallet() {
        viewModelScope.launch(Dispatchers.IO) {
            walletFlow.distinctUntilChanged().collect { prefs ->
                val wallet = prefs.wallet
                if (!wallet.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        mashupState.value = mashupState.value.copy(wallet = wallet)
                    }
                    val initialMashup =
                        collectionRepo.getMashup("0x10F418D9DaEbad69767f2Ab67d613503376d2b61")
                    withContext(Dispatchers.Main) {
                        mashupState.value = mashupState.value.copy(
                            mashupDetails = initialMashup,
                            colors = initialMashup.colors
                        )
                    }
                    stackManager.clear()

                    try {
                        isSync.value = true
                        collectionRepo.updateOwnedData("0x10F418D9DaEbad69767f2Ab67d613503376d2b61")
                    } catch (e: Exception) {
                        Timber.tag("GG").e(e, "updateOwnedData failed")
                    } finally {
                        isSync.value = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        mashupState.value = mashupState.value.copy(wallet = null)
                    }
                    collectionRepo.clearOwned()
                }
            }
        }
    }

    private fun observeCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            collectionFlow.distinctUntilChanged().collect { collection ->
                withContext(Dispatchers.Main) {
                    mashupUiState.value = mashupUiState.value.copy(isCollectionReady = true)
                    mashupState.value = mashupState.value.copy(nfts = collection.fromEntities())
                }
            }
        }
    }

    // State
    private fun recordState() {
        stackManager.record(mashupState.value.mashupDetails)
    }

    fun onUndo() {
        stackManager.undo(mashupState.value.mashupDetails)?.let { previous ->
            mashupState.value = mashupState.value.copy(
                mashupDetails = previous,
                colors = previous.colors
            )
        }
    }

    fun onRedo() {
        stackManager.redo(mashupState.value.mashupDetails)?.let { next ->
            mashupState.value = mashupState.value.copy(
                mashupDetails = next,
                colors = next.colors
            )
        }
    }

    // Collection
    fun changeSortType(
        scope: CoroutineScope,
        vState: LazyListState,
        gState: LazyGridState,
        type: SortType
    ) {
        mashupState.value = mashupState.value.copy(sortType = type)
        scope.launch {
            vState.animateScrollToItem(0)
            gState.animateScrollToItem(0)
        }
    }

    // Mashup
    fun onReset() {
        recordState()
        mashupState.value = mashupState.value.copy(mashupDetails = MashupDetails())
    }

    fun onMashupUpdate(mashupTrait: MashupTrait) {
        val uiState = mashupState.value
        val mashupDetails = uiState.mashupDetails

        recordState()

        val trait = mashupTrait.trait
        val assets = mashupDetails.assets.toMutableList()

        val assetIndex = assets.indexOfFirst { it.type == trait.type }
        if (assetIndex != -1) {
            if (assets[assetIndex].url != trait.url) {
                assets[assetIndex] = trait
            } else {
                assets[assetIndex] = assets[assetIndex].copy(url = null)
            }
        }

        mashupState.value = uiState.copy(
            mashupDetails = mashupDetails.copy(
                assets = assets,
            )
        )
    }

    fun onRandom() {
        val uiState = mashupState.value
        if (uiState.nfts.isEmpty()) return

        recordState()

        val randomAssets = getRandomTraits(uiState.nfts)
        mashupState.value = uiState.copy(
            mashupDetails = uiState.mashupDetails.copy(
                assets = randomAssets.map { it.trait },
            )
        )
    }

    fun onSave() {
        viewModelScope.launch(Dispatchers.IO) {
            mashupUiState.value = mashupUiState.value.copy(isSave = true)
            val uiState = mashupState.value
            if (uiState.wallet.isNullOrEmpty()) return@launch

            val res = mashitRepo.saveMashup(
                wallet = uiState.wallet,
                mashupDetails = uiState.mashupDetails
            )

            val dialogContent = if (res?.success == true) {
                DialogContent(
                    title = "Mashup Saved",
                    text = "Enjoy sharing it with friends"
                )
            } else {
                DialogContent(title = "Save Error", text = "Please try again later")
            }

            mashupUiState.value = mashupUiState.value.copy(
                dialogContent = dialogContent,
                isSave = false
            )
        }
    }

    // Colors
    fun onColorsSave() {
        recordState()
        val uiState = mashupState.value

        mashupState.value = uiState.copy(
            mashupDetails = uiState.mashupDetails.copy(
                colors = uiState.colors
            )
        )
    }

    fun onColorsReset() {
        mashupState.value = mashupState.value.copy(
            colors = mashupState.value.mashupDetails.colors
        )
    }

    fun onColorTypeSelect(colorType: ColorType) {
        mashupState.value = mashupState.value.copy(selectedColorType = colorType)
    }

    fun onColorChange(color: Color) {
        val uiState = mashupState.value

        val hex = "#" + color.toHexString()
        val currentColors = uiState.colors

        mashupState.value = uiState.copy(
            colors = when (uiState.selectedColorType) {
                ColorType.BASE -> currentColors.copy(base = hex)
                ColorType.EYES -> currentColors.copy(eyes = hex)
                ColorType.HAIR -> currentColors.copy(hair = hex)
            }
        )
    }

    // Category
    fun onCategorySelect(scope: CoroutineScope, state: LazyGridState, selectedCategory: TraitType) {
        mashupState.value = mashupState.value.copy(
            selectedCategory = selectedCategory,
        )
        mashupUiState.value = mashupUiState.value.copy(
            isCollectibles = false
        )
        scope.launch { state.scrollToItem(0) }
    }

    // Images
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

    fun onImageSave(downloadType: DownloadType) {
        mashupState.value.wallet?.let { _ ->
            val assets = mashupState.value.mashupDetails.assets
            val layers = assets
                .filter { it.url != null }
                .map { asset ->
                    MashupLayer(
                        name = asset.type.name.lowercase(),
                        image = asset.url!!.toIpfsUri()
                    )
                }

            val colors = mashupState.value.mashupDetails.colors
            val mashupColors = MashupColors(
                base = colors.base,
                eyes = colors.eyes,
                hair = colors.hair
            )

            val body = GenerateMashupReq(
                assets = layers,
                colors = mashupColors
            )

            Timber.tag("GG").d(body.toString())
            val jsonString = Json.encodeToString(body)
            Timber.tag("GG").d(jsonString)

            val name = mashupState.value.mashupDetails.name
            val mintedName = if (mashupState.value.selectedMint == null || name == null) {
                ""
            } else {
                if (name.length >= 17) {
                    name.substring(0, 15) + "..." + " #${mashupState.value.selectedMint}"
                } else {
                    name + " #${mashupState.value.selectedMint}"
                }
            }

            startImageDownload(
                jsonString,
                downloadType.type,
                worker = worker,
                mintedName = mintedName
            )
        }
    }

    fun updateSelectedMint(mint: Int? = null) {
        mashupState.value = mashupState.value.copy(selectedMint = mint)
    }

    fun updateName(name: String?) {
        val details = mashupState.value.mashupDetails
        mashupState.value = mashupState.value.copy(
            mashupDetails = details.copy(name = name)
        )
    }

    // Intents
    fun processActionsIntent(intent: ActionsIntent) {
        val uiState = mashupUiState.value

        when (intent) {
            is ActionsIntent.OnColor -> mashupUiState.value =
                uiState.copy(isColorChange = true)

            is ActionsIntent.OnColorDismiss -> mashupUiState.value =
                uiState.copy(isColorChange = false)

            is ActionsIntent.OnPreview -> mashupUiState.value =
                uiState.copy(isPreview = true)

            is ActionsIntent.OnPreviewDismiss -> mashupUiState.value =
                uiState.copy(isPreview = false)

            is ActionsIntent.OnImageSave -> onImageSave(intent.downloadType)
            is ActionsIntent.OnRandom -> onRandom()
            is ActionsIntent.OnSave -> onSave()
            is ActionsIntent.OnReset -> onReset()
            is ActionsIntent.OnRedo -> onRedo()
            is ActionsIntent.OnUndo -> onUndo()
        }
    }

    fun onCollectiblesSelect() {
        mashupUiState.value = mashupUiState.value.copy(
            isCollectibles = true
        )
    }

    fun onCollectibleExpand(
        position: Int,
        scope: CoroutineScope,
        state: LazyListState
    ) {
        scope.launch { state.animateScrollToItem(position) }
    }

    fun processMashupIntent(intent: MashupIntent) {
        when (intent) {
            is MashupIntent.OnCategorySelect -> onCategorySelect(
                intent.scope,
                intent.state,
                intent.selected
            )

            is MashupIntent.OnCollectibleExpand -> onCollectibleExpand(
                intent.position,
                intent.scope,
                intent.state
            )

            is MashupIntent.OnCollectiblesSelect -> onCollectiblesSelect()
            is MashupIntent.OnColorChange -> onColorChange(intent.color)
            is MashupIntent.OnColorsReset -> onColorsReset()
            is MashupIntent.OnMashupUpdate -> onMashupUpdate(intent.trait)
            is MashupIntent.OnColorsSave -> onColorsSave()
            is MashupIntent.OnColorTypeSelect -> onColorTypeSelect(intent.colorType)
        }
    }

    fun processDialogIntent(intent: DialogIntent) {
        when (intent) {
            is DialogIntent.OnClear -> mashupUiState.value =
                mashupUiState.value.copy(dialogContent = null)

            is DialogIntent.OnChange -> mashupUiState.value =
                mashupUiState.value.copy(dialogContent = intent.content)
        }
    }

    fun processImageIntent(intent: ImageIntent) {
        when (intent) {
            is ImageIntent.OnTypeGet -> getImageType(intent.url, intent.onResult)
            is ImageIntent.OnTypeSet -> setImageType(intent.url, intent.type)
        }
    }
}