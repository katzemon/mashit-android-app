package com.mashiverse.mashit.nav.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class MashupRoutes {

    @Serializable
    data object Builder : MashupRoutes()

    @Serializable
    data object Settings : MashupRoutes()
}