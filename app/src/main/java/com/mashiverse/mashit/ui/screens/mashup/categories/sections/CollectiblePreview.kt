package com.mashiverse.mashit.ui.screens.mashup.categories.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.Trait
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.states.mashup.MashupIntent
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.ui.default.traits.TraitHolder
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.MediumPadding
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.Secondary
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.ui.theme.TraitShape
import com.mashiverse.mashit.utils.helpers.sys.detectScreenType
import com.mashiverse.mashit.utils.helpers.sys.getItemWidth
import kotlinx.coroutines.CoroutineScope

@Composable
fun CollectiblePreview(
    isMoreTraits: Boolean = false,
    nft: Nft,
    mashupDetails: MashupDetails,
    position: Int,
    scope: CoroutineScope,
    state: LazyListState,
    processMashupIntent: (MashupIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val config = LocalConfiguration.current
    val screenType = config.detectScreenType()

    val isSelected = { trait: Trait ->
        val sameTypeTrait = mashupDetails.assets.find { it.type == trait.type }
        sameTypeTrait?.url == trait.url
    }

    val (hPadding, vPadding, columns) = if (isMoreTraits) {
        Triple(7.5.dp, 8.dp, screenType.collectionColumns / 3 * 4)
    } else {
        Triple(11.5.dp, MediumPadding, screenType.collectionColumns)
    }

    BoxWithConstraints {
        val constraints = this

        val width = getItemWidth(
            columns,
            maxWidth = constraints.maxWidth,
            vPadding,
            initialPadding = -Padding
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier

                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24))
                    .clickable {
                        isExpanded = !isExpanded
                    }
                    .background(Secondary.copy(alpha = 0.33f)),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .clip(RoundedCornerShape(12))
                        .background(Secondary)
                        .padding(SmallPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .height(48.dp)
                            .clip(TraitShape),
                        model = nft.compositeUrl,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(SmallPadding))

                    Spacer(modifier = Modifier.weight(1F))

                    Text(
                        text = nft.name,
                        fontSize = 14.sp,
                        color = ContentAccentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.weight(1F))

                    Spacer(modifier = Modifier.width(SmallPadding))

                    IconButton(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        onClick = {
                            isExpanded = !isExpanded
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp),
                            tint = ContentAccentColor,
                            imageVector = if (!isExpanded) Icons.Default.ArrowCircleDown else Icons.Default.ArrowCircleUp,
                            contentDescription = null
                        )
                    }
                }
            }


            AnimatedVisibility(isExpanded) {
                Column(
                    modifier = Modifier.onPlaced {
                        processMashupIntent.invoke(
                            MashupIntent.OnCollectibleExpand(
                                state = state,
                                scope = scope,
                                position = position
                            )
                        )
                    }
                ) {
                    Spacer(modifier = Modifier.height(MediumPadding))

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalArrangement = Arrangement.spacedBy(vPadding),
                        horizontalArrangement = Arrangement.spacedBy(hPadding),
                        maxItemsInEachRow = columns
                    ) {
                        nft.traits?.forEach { trait ->
                            TraitHolder(
                                isMoreTraits = isMoreTraits,
                                modifier = Modifier
                                    .width(width),
                                isSelected = isSelected.invoke(trait),
                                trait = trait,
                                processImageIntent = processImageIntent,
                                onClick = {
                                    processMashupIntent.invoke(
                                        MashupIntent.OnMashupUpdate(
                                            MashupTrait(trait = trait, avatarName = nft.name)
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}