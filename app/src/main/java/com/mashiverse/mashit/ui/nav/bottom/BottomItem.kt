package com.mashiverse.mashit.ui.nav.bottom

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.Secondary

@Composable
fun BottomItem(painterRes: Int, selected: Boolean, onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.size(40.dp),
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors().copy(containerColor = if (selected) {
            Secondary
        } else {Color.Transparent})
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(painterRes),
            contentDescription = null,
            tint = ContentAccentColor
        )
    }
}