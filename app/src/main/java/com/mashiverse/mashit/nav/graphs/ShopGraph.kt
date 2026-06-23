package com.mashiverse.mashit.nav.graphs

import androidx.compose.runtime.State
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mashiverse.mashit.nav.routes.ShopRoutes
import com.mashiverse.mashit.ui.screens.shop.regular.RegularShop
import com.mashiverse.mashit.ui.screens.shop.special.SpecialDrop

fun NavGraphBuilder.shopGraph(
    searchQuery: State<String>,
    clearSearchQuery: () -> Unit,
    navigateToDrop: (String) -> Unit,
) {
    composable<ShopRoutes.RegularShop> { backStackEntry ->
        val regularRoute: ShopRoutes.RegularShop = backStackEntry.toRoute()
        val listingId = regularRoute.listingId

        RegularShop(
            searchQuery = searchQuery,
            clearSearchQuery = clearSearchQuery,
            listingId = listingId,
            navigateToDrop = navigateToDrop,
        )
    }

    composable<ShopRoutes.SpecialShop> { backStackEntry ->
        val specialRoute: ShopRoutes.SpecialShop = backStackEntry.toRoute()
        val slug = specialRoute.slug

        SpecialDrop(slug = slug)
    }
}