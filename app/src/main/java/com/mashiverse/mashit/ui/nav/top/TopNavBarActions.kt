package com.mashiverse.mashit.ui.nav.top

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mashiverse.mashit.ui.nav.search.SearchBar

import com.mashiverse.mashit.ui.theme.Padding

@Composable
fun TopNavBarActions(
    modifier: Modifier = Modifier,
    isSearch: Boolean,
    onIsSearchChange: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .padding(horizontal = Padding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1F))

        SearchBar(
            isSearch = isSearch,
            onIsSearchChange = onIsSearchChange,
            searchQuery = searchQuery,
            onSearchQueryChange
        )
    }
}