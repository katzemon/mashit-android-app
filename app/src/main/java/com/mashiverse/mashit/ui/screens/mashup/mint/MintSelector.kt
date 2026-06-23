package com.mashiverse.mashit.ui.screens.mashup.mint

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.ui.theme.ActiveButtonBackground
import com.mashiverse.mashit.ui.theme.ButtonBackground
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding

@Composable
fun MintSelector(
    modifier: Modifier = Modifier,
    mints: List<Int>,
    selectedMint: Int?,
    onMintSelect: (Int?) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var isVisible by remember { mutableStateOf(false) }

        IconButton(
            modifier = Modifier
                .size(32.dp),
            colors = IconButtonDefaults.iconButtonColors().copy(
                containerColor = Color.Transparent,
                contentColor = ContentAccentColor
            ),
            onClick = { isVisible = !isVisible }
        ) {
            Icon(
                imageVector = Icons.Default.Numbers,
                tint = ContentAccentColor,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(Padding))

        AnimatedVisibility(
            visible = isVisible
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SmallPadding)
            ) {
                item {
                    Button(
                        modifier = Modifier
                            .height(32.dp),
                        onClick = { onMintSelect.invoke(null) },
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = if (selectedMint == null) {
                                ActiveButtonBackground
                            } else {
                                ButtonBackground
                            },
                            contentColor = if (selectedMint == null) {
                                ContentAccentColor
                            } else {
                                ContentColor
                            }
                        ),
                        contentPadding = PaddingValues(horizontal = Padding)
                    ) {
                        Text(
                            text = "None",
                            fontSize = 14.sp,
                        )
                    }
                }

                items(mints) { mint ->
                    Button(
                        modifier = Modifier
                            .height(32.dp),
                        onClick = {
                            onMintSelect.invoke(mint)
                        },
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = if (selectedMint == mint) {
                                ActiveButtonBackground
                            } else {
                                ButtonBackground
                            },
                            contentColor = if (selectedMint == mint) {
                                ContentAccentColor
                            } else {
                                ContentColor
                            }
                        ),
                        contentPadding = PaddingValues(horizontal = Padding)
                    ) {
                        Text(
                            text = "${mint}",
                            fontSize = 14.sp,
                        )
                    }
                }
            }
        }
    }
}