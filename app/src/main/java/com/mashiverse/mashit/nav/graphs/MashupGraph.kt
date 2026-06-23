package com.mashiverse.mashit.nav.graphs

import androidx.compose.runtime.State
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mashiverse.mashit.nav.routes.MashupRoutes
import com.mashiverse.mashit.nav.routes.ShopRoutes
import com.mashiverse.mashit.ui.screens.mashup.MashupBuilder
import com.mashiverse.mashit.ui.screens.settings.Settings
import com.mashiverse.mashit.ui.screens.shop.special.SpecialDrop

fun NavGraphBuilder.mashupGraph(
    searchQ: State<String>,
    openSettings: () -> Unit
) {
    composable<MashupRoutes.Builder> {
        MashupBuilder(searchQuery = searchQ, openSettings = openSettings)
    }

    composable<MashupRoutes.Settings> {
        Settings()
    }
}