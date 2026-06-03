package com.mashiverse.mashit.data.models.mashup.generation

import com.mashiverse.mashit.data.models.mashup.save.MashupColors
import com.mashiverse.mashit.data.models.mashup.save.MashupLayer
import kotlinx.serialization.Serializable

@Serializable
data class GenerateMashupReq(
    val assets: List<MashupLayer>,
    val colors: MashupColors
)