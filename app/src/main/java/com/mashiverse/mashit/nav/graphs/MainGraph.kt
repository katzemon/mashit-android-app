package com.mashiverse.mashit.nav.graphs

import androidx.compose.runtime.State
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.mashiverse.mashit.nav.routes.MainRoutes
import com.mashiverse.mashit.ui.screens.artists.Artists
import com.mashiverse.mashit.ui.screens.collection.Collection
import com.mashiverse.mashit.ui.screens.mashup.Mashup
import com.mashiverse.mashit.ui.screens.mashup.MashupBuilder
import com.mashiverse.mashit.ui.screens.settings.Settings
import com.mashiverse.mashit.ui.screens.shop.Shop

fun NavGraphBuilder.mainGraph(
    searchQuery: State<String>,
    clearSearchQuery: () -> Unit,
) {
    composable<MainRoutes.Shop>(
        deepLinks = listOf(
            navDeepLink<MainRoutes.Shop>(
                basePath = "https://www.mash-it.io"
            ) {
                uriPattern = "https://www.mash-it.io/shop/{listingId}"
            }
        )
    ) { backStackEntry ->
        val shopRoute: MainRoutes.Shop = backStackEntry.toRoute()
        val listingId = shopRoute.listingId
        Shop(listingId = listingId, searchQuery = searchQuery, clearSearchQuery = clearSearchQuery)
    }

    composable<MainRoutes.Artists> {
        Artists()
    }

    composable<MainRoutes.Collection> {
        Collection(searchQuery)
    }

    composable<MainRoutes.Mashup> {
        Mashup(searchQuery)
    }
}