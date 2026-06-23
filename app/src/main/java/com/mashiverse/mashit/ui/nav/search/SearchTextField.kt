package com.mashiverse.mashit.ui.nav.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.ui.theme.ContentAccentColor

@Composable
fun SearchTextField(
    width: Dp,
    onIsSearchChange: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    TextField(
        modifier = Modifier
            .width(width),
        value = searchQuery,
        onValueChange = { input -> onSearchQueryChange.invoke(input) },
        leadingIcon = {
            Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onIsSearchChange.invoke()
                        },
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon",
                    tint = ContentAccentColor
                )
            }
        },
        trailingIcon = {
            Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onIsSearchChange.invoke()
                            onSearchQueryChange.invoke("")
                        },
                    imageVector = Icons.Default.Clear,
                    tint = ContentAccentColor,
                    contentDescription = ""
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onIsSearchChange.invoke()
                defaultKeyboardAction(ImeAction.Done)
            }
        ),
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        ),
        singleLine = true,
        textStyle = TextStyle(fontSize = 16.sp),
        placeholder = { Text(text = "Search Mash-it", fontSize = 16.sp) }
    )
}