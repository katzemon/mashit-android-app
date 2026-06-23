package com.mashiverse.mashit.ui.nav.search

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.Primary
import com.mashiverse.mashit.ui.theme.SearchHeight
import com.mashiverse.mashit.ui.theme.SearchShape

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SearchBar(
    isSearch: Boolean,
    onIsSearchChange: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    val config = LocalConfiguration.current

    val width = animateDpAsState(
        targetValue = if (isSearch) {
            config.screenWidthDp.dp - 32.dp
        } else {
            SearchHeight
        }
    )

    val borderColor = animateColorAsState(
        targetValue = if (isSearch) {
            Primary
        } else {
            Color.Transparent
        },
        animationSpec = if (isSearch) {
            tween(durationMillis = 300)
        } else {
            tween(durationMillis = 250)
        }
    )

    Row(
        modifier = Modifier
            .height(SearchHeight)
            .width(width.value)
            .clip(shape = SearchShape)
            .border(
                border = BorderStroke(width = 1.dp, color = borderColor.value),
                shape = SearchShape
            )
            .wrapContentSize(unbounded = true, align = Alignment.CenterStart)
            .clipToBounds(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        if (width.value <= SearchHeight * 4) {
            Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onIsSearchChange.invoke() },
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon",
                    tint = ContentAccentColor
                )
            }
        } else {
            SearchTextField(
                width = width.value,
                onIsSearchChange = onIsSearchChange,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange
            )
        }
    }
}