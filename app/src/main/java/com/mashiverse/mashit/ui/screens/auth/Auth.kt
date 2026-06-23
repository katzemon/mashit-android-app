package com.mashiverse.mashit.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.R
import com.mashiverse.mashit.data.models.sys.wallet.WalletPreferences
import com.mashiverse.mashit.ui.theme.Background
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ContentContainerShape
import com.mashiverse.mashit.ui.theme.Padding


@Composable
fun Auth(onLurking: () -> Unit, onAuth: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val resId = listOf(
            R.drawable.mashup,
            R.drawable.mashup2,
            R.drawable.mashup3,
            R.drawable.mashup4
        ).random()

        Image(
            modifier = Modifier
                .fillMaxSize()
                .blur(
                    13.dp
                ),
            contentDescription = null,
            painter = painterResource(resId),
            contentScale = ContentScale.FillHeight
        )

        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {


            Row(modifier = Modifier.width(256.dp)) {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors().copy(
                        containerColor = Color.Transparent,
                        contentColor = ContentAccentColor,

                        ),
                    border = BorderStroke(width = 1.dp, Color.White),
                    shape = ContentContainerShape,
                    onClick = onAuth,
                    contentPadding = PaddingValues(horizontal = Padding)
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Connect wallet",
                        textAlign = TextAlign.Start,
                        fontSize = 24.sp
                    )
                }
            }
            Row(modifier = Modifier.width(256.dp)) {
                Spacer(Modifier.weight(1f))

                TextButton(onClick = onLurking) {
                    Text("or start lurking", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(256.dp))
        }

        Text(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(bottom = 8.dp, end = 8.dp),
            text = "mashup by u/Snek",
            color = ContentAccentColor.copy(alpha = 0.5F),
            fontSize = 10.sp
        )

    }
}

@Composable
@Preview
private fun AuthPreview() {
    Box(modifier = Modifier.background(Background)) {
        Auth(
            onLurking = {},
            onAuth = {},
        )
    }
}