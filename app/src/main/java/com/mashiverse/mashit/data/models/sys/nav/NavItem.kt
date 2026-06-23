package com.mashiverse.mashit.data.models.sys.nav

import com.mashiverse.mashit.R
import com.mashiverse.mashit.nav.routes.MainRoutes
import kotlinx.serialization.Serializable

@Serializable
data class NavItem(
    val label: String,
    val route: MainRoutes,
    val icon: Int
)

val navItems = listOf(
    NavItem("Shop", MainRoutes.Shop(listingId = null), R.drawable.shop_icon),
    NavItem("Artists", MainRoutes.Artists, R.drawable.artist_icon),
    NavItem("Collection", MainRoutes.Collection, R.drawable.collection_icon),
    NavItem("Mashup", MainRoutes.Mashup, R.drawable.mashup_icon)
)