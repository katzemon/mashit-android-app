package com.mashiverse.mashit.ui.screens.mashup.actions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.sys.image.DownloadType
import com.mashiverse.mashit.data.states.mashup.ActionsIntent
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.ui.default.buttons.DiscordButton
import com.mashiverse.mashit.ui.default.buttons.RedditButton
import com.mashiverse.mashit.ui.default.traits.MashupComposite
import com.mashiverse.mashit.ui.default.traits.MintText
import com.mashiverse.mashit.ui.screens.mashup.actions.buttons.ActionButton
import com.mashiverse.mashit.ui.screens.mashup.actions.buttons.SaveActionButton
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.ui.theme.Surface
import com.mashiverse.mashit.ui.theme.TraitShape
import com.mashiverse.mashit.ui.theme.XLHolderHeight

@Composable
fun MashupActions(
    mashupDetails: MashupDetails,
    modifier: Modifier = Modifier,
    holderWidth: Dp,
    processImageIntent: (ImageIntent) -> Unit,
    processActionsIntent: (ActionsIntent) -> Unit,
    selectedMint: Int?
) {
    var isOpened by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier
                    .width(70.dp)
                    .height(XLHolderHeight),
                verticalArrangement = Arrangement.Bottom
            ) {
//                RedditButton()
//
//                Spacer(modifier = Modifier.height(SmallPadding))
//
//                DiscordButton()
//
//                Spacer(modifier = Modifier.height(SmallPadding))


            }

            Spacer(Modifier.width(SmallPadding))

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(TraitShape)
                    .background(Surface),
            ) {
                MashupComposite(
                    modifier = modifier,
                    colors = mashupDetails.colors,
                    assets = mashupDetails.assets,
                    holderWidth = holderWidth,
                    processImageIntent = processImageIntent
                )

                selectedMint?.let {
                    MintText(
                        modifier = Modifier
                            .padding(bottom = 3.dp, end = 6.dp)
                            .align(Alignment.BottomEnd),
                        mint = selectedMint,
                        fontSize = 16
                    )
                }
            }


            Spacer(Modifier.width(SmallPadding))

            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .width(holderWidth + (40 * 2).dp + SmallPadding * 2),
                horizontalArrangement = Arrangement.End
            ) {
                Column {
                    SaveActionButton(onSave = { processActionsIntent(ActionsIntent.OnSave) })

                    AnimatedVisibility(visible = isOpened) {
                        Column {
                            Spacer(modifier = Modifier.height(SmallPadding))

                            ActionButton(
                                icon = Icons.Default.Download,
                                text = "PNG",
                                onClick = {
                                    processActionsIntent(
                                        ActionsIntent.OnImageSave(
                                            downloadType = DownloadType.PNG
                                        )
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(SmallPadding))

                            ActionButton(
                                icon = Icons.Default.Download,
                                text = "GIF",
                                onClick = {
                                    processActionsIntent(
                                        ActionsIntent.OnImageSave(
                                            downloadType = DownloadType.GIF
                                        )
                                    )
                                },
                                isAnimated = true
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(SmallPadding))

        ActionsPanel(processActionsIntent = processActionsIntent, isExpanded = isOpened, onExpand = { isOpened = !isOpened})
    }
}