package com.mashiverse.mashit.ui.default.images

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.mashiverse.mashit.data.models.sys.image.ImageType
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.data.states.utils.rememberImageType
import com.mashiverse.mashit.ui.theme.TraitShape
import com.mashiverse.mashit.utils.MASHIVERSE_BASE_URL


@Composable
fun DefaultImage(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    data: String,
    processImageIntent: (ImageIntent) -> Unit,
    selectedColors: SelectedColors? = null,
    contentScale: ContentScale = ContentScale.FillHeight
) {

    val imageType by rememberImageType(
        data = data.replace("https://ipfs.filebase.io/", "https://round-peach-hippopotamus.myfilebase.com/"),
        processImageIntent = processImageIntent
    )

    Box(
        modifier = modifier
            .clip(TraitShape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (imageType != null) {
            when (imageType) {
                ImageType.SVG, ImageType.SVG_MASK -> {
                    val newData = when (imageType) {
                        ImageType.SVG -> data.replace("https://ipfs.filebase.io/", "https://round-peach-hippopotamus.myfilebase.com/")
                        ImageType.SVG_MASK -> "${MASHIVERSE_BASE_URL}api/svg/${
                            data.split("/").last()
                        }"

                        else -> ""
                    }
                    SvgImage(
                        modifier = modifier.fillMaxSize(),
                        data = newData,
                        selectedColors = selectedColors,
                        contentScale = contentScale
                    )
                }

                ImageType.APNG -> {
                    val newData = "${MASHIVERSE_BASE_URL}api/apng/${data.split("/").last()}"
                    NonSvgImage(
                        modifier = modifier.fillMaxSize(),
                        data = newData,
                        contentScale = contentScale
                    )
                }

                else -> {
                    NonSvgImage(
                        modifier = modifier.fillMaxSize(),
                        data = data.replace("https://ipfs.filebase.io/", "https://round-peach-hippopotamus.myfilebase.com/") ,
                        contentScale = contentScale
                    )
                }
            }
        }
    }
}