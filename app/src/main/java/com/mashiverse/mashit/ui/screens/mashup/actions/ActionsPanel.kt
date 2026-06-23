package com.mashiverse.mashit.ui.screens.mashup.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mashiverse.mashit.data.states.mashup.ActionsIntent
import com.mashiverse.mashit.ui.screens.mashup.actions.buttons.ActionButton
import com.mashiverse.mashit.ui.screens.mashup.actions.buttons.ColorSelectActionButton
import com.mashiverse.mashit.ui.theme.SmallPadding

@Composable
fun ActionsPanel(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onExpand: () -> Unit,
    processActionsIntent: (ActionsIntent) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(SmallPadding)
//        ) {
//            ActionButton(
//                icon = Icons.Default.Delete,
//                onClick = { processActionsIntent(ActionsIntent.OnReset) },
//            )
//
//            ActionButton(
//                icon = Icons.Default.Download,
//                onClick = {
//                    processActionsIntent(
//                        ActionsIntent.OnImageSave(
//                            downloadType = DownloadType.PNG
//                        )
//                    )
//                }
//            )
//
//            ActionButton(
//                icon = Icons.Default.Download,
//                onClick = {
//                    processActionsIntent(
//                        ActionsIntent.OnImageSave(
//                            downloadType = DownloadType.GIF
//                        )
//                    )
//                },
//                isAnimated = true
//            )
//
//            SaveActionButton(onSave = { processActionsIntent(ActionsIntent.OnSave) })
//        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(SmallPadding)
        ) {
//            SaveActionButton(onSave = { processActionsIntent(ActionsIntent.OnSave) })

            ColorSelectActionButton(onColor = { processActionsIntent(ActionsIntent.OnColor) })

            ActionButton(
                icon = Icons.Default.Delete,
                onClick = { processActionsIntent(ActionsIntent.OnReset) },
            )

            ActionButton(
                icon = Icons.Default.Refresh,
                onClick = { processActionsIntent(ActionsIntent.OnRandom) }
            )
//
//            ActionButton(
//                icon = Icons.Default.Slideshow,
//                onClick = { processActionsIntent(ActionsIntent.OnPreview) },
//            )

            ActionButton(
                icon = Icons.AutoMirrored.Filled.Undo,
                onClick = { processActionsIntent(ActionsIntent.OnUndo) },
            )

            ActionButton(
                icon = Icons.AutoMirrored.Filled.Redo,
                onClick = { processActionsIntent(ActionsIntent.OnRedo) }
            )


            ActionButton(
                icon =
                    if (!isExpanded) {
                        Icons.Default.Download
                    } else {
                        Icons.Default.Clear
                    },
                onClick = onExpand,
            )
        }
    }
}