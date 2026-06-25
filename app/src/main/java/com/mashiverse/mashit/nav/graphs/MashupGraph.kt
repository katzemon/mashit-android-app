package com.mashiverse.mashit.nav.graphs

import androidx.compose.runtime.State
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mashiverse.mashit.nav.routes.MashupRoutes
import com.mashiverse.mashit.ui.screens.mashup.MashupBuilder
import com.mashiverse.mashit.ui.screens.settings.Settings

fun NavGraphBuilder.mashupGraph(
    searchQ: State<String>,
    openSettings: () -> Unit,
    onSignIn: () -> Unit
) {
    composable<MashupRoutes.Builder> {
        MashupBuilder(searchQuery = searchQ, openSettings = openSettings, onSignIn = onSignIn)
    }

    composable<MashupRoutes.Settings> {
        Settings()
    }
}