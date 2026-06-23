package com.mashiverse.mashit.ui.default.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.R
import com.mashiverse.mashit.ui.theme.ContentContainerHeight
import com.mashiverse.mashit.utils.helpers.openSocialLink

@Composable
fun RedditButton() {
    val context = LocalContext.current
    val uri = "https://www.reddit.com/r/mashit"

    IconButton(
        modifier = Modifier
            .size(40.dp)
            .height(ContentContainerHeight),
        onClick = { openSocialLink(context, uri) },
        colors = IconButtonDefaults.iconButtonColors().copy(
            Color(0xFF5865F2)
        )
    ) {
        Image(
            painter = painterResource(R.drawable.reddit),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun RedditButtonPreview() {
    RedditButton()
}