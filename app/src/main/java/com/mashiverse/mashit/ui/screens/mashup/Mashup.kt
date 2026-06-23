package com.mashiverse.mashit.ui.screens.mashup

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mashiverse.mashit.nav.graphs.mashupGraph
import com.mashiverse.mashit.nav.routes.MashupRoutes

@Composable
fun Mashup(searchQ: State<String>) {
    val innerNavController = rememberNavController()

    val openSettings = {
        innerNavController.navigate(route = MashupRoutes.Settings)
    }

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = innerNavController,
        startDestination = MashupRoutes.Builder
    ) {
        mashupGraph(
            searchQ = searchQ,
            openSettings = openSettings
        )
    }
}