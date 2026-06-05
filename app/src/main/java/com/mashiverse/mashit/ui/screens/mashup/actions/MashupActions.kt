package com.mashiverse.mashit.ui.screens.mashup.actions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.states.mashup.ActionsIntent
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.ui.default.traits.MashupComposite
import com.mashiverse.mashit.ui.theme.Surface
import com.mashiverse.mashit.ui.theme.TraitShape

@Composable
fun MashupActions(
    mashupDetails: MashupDetails,
    modifier: Modifier = Modifier,
    holderWidth: Dp,
    processImageIntent: (ImageIntent) -> Unit,
    processActionsIntent: (ActionsIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .widthIn(0.dp, 400.dp)
                .align(Alignment.Center)
        ) {
            LeftPanel(processActionsIntent = processActionsIntent)

            Spacer(modifier = Modifier.weight(1F))

            Box(
                modifier = Modifier
                    .clip(TraitShape)
                    .background(Surface)
            ) {
                MashupComposite(
                    modifier = modifier,
                    colors = mashupDetails.colors,
                    assets = mashupDetails.assets,
                    holderWidth = holderWidth,
                    processImageIntent = processImageIntent
                )
            }

            Spacer(modifier = Modifier.weight(1F))

            RightPanel(processActionsIntent = processActionsIntent)
        }
    }
}