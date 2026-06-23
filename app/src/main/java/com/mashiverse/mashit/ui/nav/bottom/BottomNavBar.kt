package com.mashiverse.mashit.ui.nav.bottom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mashiverse.mashit.data.models.artists.ArtistMashup
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.sys.nav.navItems
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.ui.nav.search.SearchBar
import com.mashiverse.mashit.ui.screens.artists.ProfilePicture
import com.mashiverse.mashit.ui.theme.Surface

@Composable
fun BottomNavBar(
    navController: NavHostController,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isSearch: Boolean,
    onIsSearchChange: () -> Unit,
    hasSearch: Boolean,
    mashup: MashupDetails,
    processImageIntent: (ImageIntent) -> Unit
) {
    var selectedDest by rememberSaveable {
        mutableIntStateOf(0)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .imePadding()
            .navigationBarsPadding()
            .height(64.dp)
            .padding(horizontal = 16.dp)

    ) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterStart),
            visible = !isSearch,
            enter = expandHorizontally() + fadeIn(),
            exit = shrinkHorizontally() + fadeOut()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    20.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                navItems.forEachIndexed { i, item ->
                    if (item.label == "Mashup") {
                        if (mashup.assets.mapNotNull { it.url }.isNotEmpty()) {
                            Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                                ProfilePicture(
                                    onClick = {
                                        selectedDest = i

                                        navController.navigate(route = item.route) {
                                            popUpTo(navController.graph.id) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    },
                                    processImageIntent = processImageIntent,
                                    artistMashup = ArtistMashup(
                                        colors = mashup.colors,
                                        layers = mashup.assets.mapNotNull { it.url }
                                    ),
                                    size = 32.dp,
                                    borderWidth = 0.dp
                                )
                            }
                        } else {
                            BottomItem(
                                painterRes = item.icon, selected = selectedDest == i
                            ) {
                                selectedDest = i

                                navController.navigate(route = item.route) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    } else {
                        if (i != 4) {
                            BottomItem(painterRes = item.icon, selected = selectedDest == i) {
                                selectedDest = i

                                navController.navigate(route = item.route) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }
            }
        }

        if (hasSearch) {
            Box(Modifier.align(Alignment.CenterEnd)) {
                SearchBar(
                    isSearch = isSearch,
                    onIsSearchChange = onIsSearchChange,
                    searchQuery = searchQuery,
                    onSearchQueryChange
                )
            }
        }
    }
}