package com.mashiverse.mashit.ui.default.images

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.SmallIconSize
import com.mashiverse.mashit.ui.theme.TraitShape
import com.mashiverse.mashit.utils.decoders.SvgCustomDecoder

@Composable
fun SvgImage(
    modifier: Modifier,
    data: String,
    selectedColors: SelectedColors?,
    contentScale: ContentScale,
    isPreview: Boolean = false
) {
    val ctx = LocalContext.current

    var cachedPainter by remember { mutableStateOf<Painter?>(null) }

    val svgLoader = remember(ctx, selectedColors) {
        ImageLoader.Builder(ctx)
            .components {
                add(SvgCustomDecoder.Factory(selectedColors = selectedColors))
                add(SvgDecoder.Factory())
            }
            .build()
    }

    val request = remember(data) {
        ImageRequest.Builder(ctx)
            .data(data)
            .crossfade(false)
            .build()
    }

    Box(
        modifier = modifier
            .clip(TraitShape)
    ) {
        cachedPainter?.let {
            Image(
                painter = it,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = contentScale,
            )
        }

        AsyncImage(
            model = request,
            imageLoader = svgLoader,
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = contentScale,
            onState = { state ->
                if (state is AsyncImagePainter.State.Success) {
                    cachedPainter = state.painter
                }
            }
        )

        if (isPreview) {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(SmallIconSize)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithContent {
                        drawContent() // Draws the brush icon first
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF9C27B0), Color(0xFF2196F3)), // purple to blue
                                start = Offset(0f, 0f), // topLeading
                                end = Offset(size.width, size.height) // bottomTrailing
                            ),
                            blendMode = BlendMode.SrcIn // Cuts the gradient to match the icon's shape
                        )
                    },
                imageVector = Icons.Default.Brush,
                // 3. Crucial: Tint must be White so the gradient colors show through accurately
                tint = Color.White,
                contentDescription = null
            )
        }
    }
}