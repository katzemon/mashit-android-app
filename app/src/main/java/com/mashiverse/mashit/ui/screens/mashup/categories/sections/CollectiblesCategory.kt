package com.mashiverse.mashit.ui.screens.mashup.categories.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.states.mashup.MashupIntent
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.ui.theme.Padding
import kotlinx.coroutines.CoroutineScope

@Composable
fun CollectiblesCategory(
    nfts: List<Nft>,
    state: LazyListState,
    mashupDetails: MashupDetails,
    scope: CoroutineScope,
    processMashupIntent: (MashupIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        state = state,
        verticalArrangement = Arrangement.spacedBy(Padding)
    ) {
        items(nfts.size) { i ->
            val nft = nfts[i]

            CollectiblePreview(
                nft = nft,
                position = i,
                state = state,
                scope = scope,
                mashupDetails = mashupDetails,
                processMashupIntent = processMashupIntent,
                processImageIntent = processImageIntent
            )
        }
    }
}