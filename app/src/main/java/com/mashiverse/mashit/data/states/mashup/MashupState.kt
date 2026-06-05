package com.mashiverse.mashit.data.states.mashup

import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.SortType
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.data.models.mashup.colors.SelectedColors

data class MashupState(
    val wallet: String? = null,
    val nfts: List<Nft> = emptyList(),
    val mashupDetails: MashupDetails = MashupDetails(),
    val selectedColorType: ColorType = ColorType.BASE,
    val selectedCategory: TraitType = TraitType.BACKGROUND,
    val sortType: SortType = SortType.NEWEST,
    val colors: SelectedColors = SelectedColors(),
    val selectedMint: Int? = null
)