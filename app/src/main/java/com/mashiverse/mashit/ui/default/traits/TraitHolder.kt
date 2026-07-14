package com.mashiverse.mashit.ui.default.traits

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.mashi.Trait
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.ui.default.images.DefaultImage
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Primary
import com.mashiverse.mashit.ui.theme.TraitShape

@Composable
fun TraitHolder(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    trait: Trait,
    isSelected: Boolean = false,
    processImageIntent: (ImageIntent) -> Unit,
    isMoreTraits: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .border(
                    width = 1.dp,
                    shape = TraitShape,
                    color = if (isSelected) Primary else ContentColor
                )
                .padding(4.dp),
        ) {
            DefaultImage(
                onClick = onClick,
                data = trait.url ?: "",
                processImageIntent = processImageIntent,
            )
        }

        if (!isMoreTraits) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = trait.type.name
                    .lowercase()
                    .replace("_", " ")
                    .replaceFirstChar { c -> c.uppercaseChar() },
                color = ContentAccentColor,
                fontSize = 12.sp
            )
        }
    }
}