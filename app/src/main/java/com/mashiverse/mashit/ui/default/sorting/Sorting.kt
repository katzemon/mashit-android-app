package com.mashiverse.mashit.ui.default.sorting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.mashi.SortType
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor

@Composable
fun Sorting(
    onSortChange: (SortType) -> Unit
) {
    var isOpened by remember { mutableStateOf(false) }
    var selected by remember { mutableIntStateOf(0) }

    Row() {
        IconButton(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(32.dp),
            colors = IconButtonDefaults.iconButtonColors().copy(
                containerColor = Color.Transparent,
                contentColor = ContentAccentColor
            ),
            onClick = { isOpened = !isOpened }
        ) {
            Icon(
                imageVector = Icons.Default.SortByAlpha,
                tint = ContentAccentColor,
                contentDescription = null
            )
        }

        AnimatedVisibility(isOpened) {
            PrimaryTabRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                selectedTabIndex = selected,
                containerColor = Color.Transparent,
                indicator = {},
                divider = {}
            ) {
                SortType.entries.forEachIndexed { index, destination ->
                    Tab(
                        selected = selected == index,
                        onClick = {
                            onSortChange.invoke(destination)
                            selected = index
                            isOpened = false
                        },
                        text = {
                            Text(
                                text = destination.label,
                                fontSize = 14.sp,
                                fontWeight = if (selected == index) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                },
                                color = if (selected == index) {
                                    ContentAccentColor
                                } else {
                                    ContentColor
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun SortingPreview() {
    Sorting({})
}