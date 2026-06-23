package com.mashiverse.mashit.ui.screens.shop.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.drops.DropDetails
import com.mashiverse.mashit.ui.default.indicators.LoadingIndicator
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraSmallPadding
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding

@Composable
fun Drops(
    specialDrops: List<DropDetails>,
    navigateToDrop: (String) -> Unit
) {
    Column {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height((63.6 * 2).dp),
            horizontalArrangement = Arrangement.spacedBy(SmallPadding)
        ) {
            if (specialDrops.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillParentMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        LoadingIndicator()
                    }
                }
            }

            items(
                specialDrops.size,
            ) { i ->
                val drop = specialDrops[i]

                DropImage(
                    slug = drop.slug,
                    imageUrl = drop.imageUrl
                ) {
                    navigateToDrop.invoke(drop.slug)
                }
            }
        }

        Spacer(modifier = Modifier.height(Padding))
    }
}