package com.mashiverse.mashit.ui.screens.shop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mashiverse.mashit.nav.graphs.shopGraph
import com.mashiverse.mashit.nav.routes.ShopRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop(
    searchQuery: State<String>,
    clearSearchQuery: () -> Unit,
    listingId: String?,
) {
    val innerNavController = rememberNavController()

    val navigateToDrop = { slug: String ->
        innerNavController.navigate(route = ShopRoutes.SpecialShop(slug = slug))
    }

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = innerNavController,
        startDestination = ShopRoutes.RegularShop(listingId = listingId)
    ) {
        shopGraph(
            clearSearchQuery = clearSearchQuery,
            searchQuery = searchQuery,
            navigateToDrop = navigateToDrop,
        )
    }
}