package com.mashiverse.mashit.ui.screens.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Secondary
import com.mashiverse.mashit.ui.theme.SmallPadding

@Composable
fun CheckRow(title: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${title}:",
            fontSize = 16.sp,
            color = ContentAccentColor
        )

        Spacer(modifier = Modifier.width(SmallPadding))

        Switch(
            checked = checked,
            colors = SwitchDefaults.colors().copy(
                uncheckedBorderColor = ContentColor,
                uncheckedTrackColor = Color.Transparent,
                uncheckedThumbColor = ContentAccentColor,
                checkedTrackColor = Color.Transparent,
                checkedBorderColor = ContentColor,
                checkedThumbColor = ContentAccentColor
            ),
            onCheckedChange = { checked -> onChange.invoke(checked) },
        )
    }
}