package com.mashiverse.mashit.ui.screens.mashup.actions.buttons

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.Primary
import com.mashiverse.mashit.ui.theme.SmallIconSize

@Composable
fun SaveActionButton(
    onSave: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .size(40.dp),
        shape = RoundedCornerShape(90),
        colors = IconButtonDefaults.iconButtonColors().copy(
            containerColor = Primary,
            contentColor = ContentAccentColor
        ),
        onClick = onSave
    ) {
        Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(
            SmallIconSize
        ))
    }
}